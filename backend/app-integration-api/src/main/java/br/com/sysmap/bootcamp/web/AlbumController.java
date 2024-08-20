package br.com.sysmap.bootcamp.web;


import br.com.sysmap.bootcamp.domain.entity.Album;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import br.com.sysmap.bootcamp.domain.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;


@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;


    @Operation(summary = "Buy an album")
    @PostMapping("/sale")
    public ResponseEntity<Album> sellAlbum(@RequestBody Album album) {

        try {
            album = this.albumService.saveAlbum(album);
            return ResponseEntity.ok(album);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(summary = "Get all albums from my collection")
    @GetMapping("/my-collection")
    public ResponseEntity<List<Album>> getAlbums() {

        try {
            List<Album> albums = this.albumService.getMyCollection();
            return ResponseEntity.ok(albums);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(summary = "Get all albums from Spotify service by Text parameter")
    @GetMapping("/all")
    public ResponseEntity<List<AlbumModel>> getAlbums(@RequestParam("search") String search) {

        try {
            List<AlbumModel> albums = this.albumService.getAlbums(search);
            return ResponseEntity.ok(albums);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (ParseException e) {
            return ResponseEntity.internalServerError().build();
        } catch (SpotifyWebApiException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(summary = "Remove an album by ID")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) {

        try {
            this.albumService.deleteById(id);
            return ResponseEntity.ok(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
