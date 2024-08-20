package br.com.sysmap.bootcamp.domain.service.integration;


import br.com.sysmap.bootcamp.domain.mapper.AlbumMapper;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SpotifyApi {

    private final String myClientId = "fb030e9e5e5c4f51b7f9149dca9b4a16";
    private final String myClientSecret = "1f4dcbc8ca9c4a34bae083afd05f53e8";

    private final se.michaelthelin.spotify.SpotifyApi spotifyApi = new se
                                                .michaelthelin
                                                .spotify
                                                .SpotifyApi
                                                .Builder()
                                                .setClientId(myClientId)
                                                .setClientSecret(myClientSecret)
                                                .build();

    public List<AlbumModel> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {

        spotifyApi.setAccessToken(getToken());
        return AlbumMapper.INSTANCE
                        .toModel(spotifyApi.searchAlbums(search)
                        .market(CountryCode.BR)
                        .limit(30)
                        .build().execute().getItems())
                        .stream()
                        .peek(album -> album.setValue(generateValue()))
                        .toList();

    }


    public String getToken() throws IOException, ParseException, SpotifyWebApiException {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi
                                                            .clientCredentials()
                                                            .build();
        return clientCredentialsRequest.execute().getAccessToken();
    }


    private BigDecimal generateValue() {
        double min = 12.00f;
        double max = 100.0f;
        int scale = 2;
        double randomDouble = Math.random() * (max - min) + min;
        BigDecimal randomBigDecimal = BigDecimal.valueOf(randomDouble);
        return randomBigDecimal.setScale(scale, RoundingMode.HALF_UP);
    }
}
