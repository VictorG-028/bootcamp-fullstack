package br.com.sysmap.bootcamp.domain.repository;

import br.com.sysmap.bootcamp.domain.entity.Album;
import br.com.sysmap.bootcamp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByUser(User user);
    Optional<Album> findByIdAndUser(Long id, User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM Album a WHERE a.id = :albumId AND a.user.id = :userId")
    void customDeleteAlbumByIds(Long albumId, Long userId);
}
