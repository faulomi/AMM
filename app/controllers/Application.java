package controllers;


import play.*;

import play.libs.WS;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;


import views.html.*;

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


    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public static Result share(String source, final String type, String id, String destination) {



            return (async(url("https://api.deezer.com/2.0/" + type + "/" + id).get().flatMap(new F.Function<WS.Response, F.Promise<Result>>() {
                public F.Promise<Result> apply(WS.Response response) throws Throwable {

                    String title = response.asJson().findPath("title").asText();
                    String artist = response.asJson().findPath("artist").findPath("name").asText();

                    return url("http://ws.spotify.com/search/1/" + type + ".json").setQueryParameter("q", artist + " " + title).get().map(new F.Function<Response, Result>() {
                        @Override
                        public Result apply(Response response) throws Throwable {


                            return ok(response.asJson().findPath("tracks").get(0));
                        }
                    });
                }
            })));


        }







    public static Result searchOnSpotify(final String track) {


        final F.Promise<Result> resultPromise = url("https://api.deezer.com/2.0/track/" + track).get().map(new F.Function<Response, Result>() {
            @Override
            public Result apply(Response response) throws Throwable {
                return ok(response.asJson());
            }
        });

        return async(resultPromise);


    }


}
