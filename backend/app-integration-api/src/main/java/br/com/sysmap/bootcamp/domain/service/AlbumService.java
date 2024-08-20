package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entity.Album;
import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.repository.AlbumRepository;
import br.com.sysmap.bootcamp.domain.service.integration.SpotifyApi;
import br.com.sysmap.bootcamp.dto.WalletDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Slf4j
@Service
public class AlbumService {

    private final Queue queue;
    private final RabbitTemplate template;
    private final SpotifyApi spotifyApi;
    private final AlbumRepository albumRepository;
    private final UserService userService;


    // =-= Endpoints section =-= //
    @Transactional(propagation = Propagation.REQUIRED)
    public Album saveAlbum(Album album) {

        User user = getLoggedUser();


        album.setUser(user);

        List<Album> userOwnedAlbums = this.albumRepository.findAllByUser(user);
        Stream<Long> userOwnedIds = userOwnedAlbums.stream().map(Album::getId);
        boolean alreadyExists = userOwnedIds.anyMatch(id -> id.equals(album.getId()));
        if (alreadyExists) {
            return album;
        }

        Album albumSaved = this.albumRepository.save(album);

        WalletDto walletDto = WalletDto.builder()
                                    .email(albumSaved.getUser().getEmail())
                                    .value(albumSaved.getValue())
                                    .build();

        this.template.convertAndSend(queue.getName(), walletDto);

        return albumSaved;
    }


    public List<Album> getMyCollection() {

        User user = getLoggedUser();

        return this.albumRepository.findAllByUser(user);
    }


    public List<AlbumModel> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {
        return this.spotifyApi.getAlbums(search);
    }


    public void deleteById(Long id) throws EntityNotFoundException {
        User user = getLoggedUser();
        log.info("Deleting album {} of {}", id, user.getEmail());

        this.albumRepository.findByIdAndUser(id, user).orElseThrow(
            () -> new EntityNotFoundException("Album with ID " + id + " not found for user " + user.getEmail()));

        this.albumRepository.customDeleteAlbumByIds(id, user.getId());
    }


    // =-= Util Section =-= //
    /**
     * Retrieves the currently logged-in user.
     *
     * @return The logged-in user.
     */
    private User getLoggedUser() {
        String email = SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getName();

        return userService.findByEmail(email);
    }
}
