package controllers;


import org.codehaus.jackson.JsonNode;
import play.libs.WS;
import play.libs.F;
import play.mvc.*;


import views.html.*;

import java.util.HashMap;
import java.util.Map;

import static play.libs.WS.*;

public class Application extends Controller {

    public static class Track {

        private String title;
        private String artist;

        public Track(String title, String artist) {
            this.title = title;
            this.artist = artist;
        }
    }

    public static class SearchParameters {

        public String url;
        public final Map<String, String> queryParameters = new HashMap<String, String>();
    }


    public enum Provider {
        DEEZER("https://api.deezer.com/2.0/", "https://api.deezer.com/2.0/search") {
            @Override
            public SearchParameters buildSearchParameters(Track track) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String buildLookupUrl(String type, String id) {
                return lookupBaseUrl + type +"/" +id;
            }

            @Override
            public Track getTrack(JsonNode root) {
                String title = root.findPath("title").asText();
                String artist = root.findPath("artist").findPath("name").asText();
                return new Track(title, artist);
            }

            @Override
            public String getTrackUrl(JsonNode root) {

                return null;
            }
        },
        SPOTIFY("http://ws.spotify.com/lookup/1/", "http://ws.spotify.com/search/1/") {
            @Override
            public Track getTrack(JsonNode root) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String buildLookupUrl(String type, String id) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public SearchParameters buildSearchParameters(Track track) {
                SearchParameters searchParameters = new SearchParameters();
                searchParameters.url = searchBaseUrl + "track.json";
                searchParameters.queryParameters.put("q", track.artist + " " + track.title);

                return searchParameters;
            }

            @Override
            public String getTrackUrl(JsonNode root) {

                return root.findPath("tracks").get(0).findValuesAsText("href").get(1);
            }
        };


        private Provider(String lookupBaseUrl, String searchBaseUrl) {

            this.searchBaseUrl = searchBaseUrl;
            this.lookupBaseUrl = lookupBaseUrl;
        }



        public abstract Track getTrack(JsonNode root);
        public abstract String buildLookupUrl(String type, String id);
        public abstract SearchParameters buildSearchParameters(Track track);
        public abstract String getTrackUrl(JsonNode root);
        protected String lookupBaseUrl;

        protected String searchBaseUrl;
    }


    public static Result index() {
        return ok(index.render("Your new application is ready."));


    }


    public static Result share(String source, final String type, String id, String destination) {

        final Provider sourceProvider = Provider.valueOf(source);
        final Provider destinationProvider = Provider.valueOf(destination);

        return (async(url(sourceProvider.buildLookupUrl(type,id)).get().flatMap(new F.Function<WS.Response, F.Promise<Result>>() {
            public F.Promise<Result> apply(WS.Response response) throws Throwable {

                final Track track = sourceProvider.getTrack(response.asJson());

                final SearchParameters searchParameters = destinationProvider.buildSearchParameters(track);
                WSRequestHolder request =  url(searchParameters.url);
                for(Map.Entry<String, String> parameter : searchParameters.queryParameters.entrySet()){

                        request.setQueryParameter(parameter.getKey(), parameter.getValue());
                }
                return request.get().map(new F.Function<Response, Result>() {
                    @Override
                    public Result apply(Response response) throws Throwable {


                        return redirect(destinationProvider.getTrackUrl(response.asJson()));
                    }
                });
            }
        })));


    }




}
