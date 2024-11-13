package com.youngtfy.server.common;

import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerAPI {
    private static final String clientId = "1a26be890a3b4afe9f6527c966be92a9";
    private static final String clientSecret = "732a1e70ef2e468e8ca067e1bd98a3b6";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8888/callback"); // 여기에 리다이렉트 URI 입력

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static void clientCredentials_Sync() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String[] searchTrackName(String trackName) {
        clientCredentials_Sync();
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(trackName).build();

        try {
            final Track[] tracks = searchTracksRequest.execute().getItems();

            if (tracks.length > 0) {
                Track firstTrack = tracks[0];
                String imageUrl = firstTrack.getAlbum().getImages()[0].getUrl();
                String musicName = firstTrack.getName();
                String musicId = firstTrack.getId();
                System.out.println(musicId);

                String[] data = {imageUrl, firstTrack.getPreviewUrl(), musicName, musicId};
                return data;
            } else {
                System.out.println("No tracks found for the search query.");
                return null;
            }
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public String searchTrackId(String trackId) {
        clientCredentials_Sync();
        if (trackId == null) return null;
        try {
            GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();
            final Track track = getTrackRequest.execute();
            System.out.println(track);
            if (track != null) {
                return track.getName();
            } else {
                System.out.println("No tracks found for the search query.");
                return null;
            }
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
