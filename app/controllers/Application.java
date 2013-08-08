package controllers;


import models.Provider;
import models.Query;
import models.ShareRecord;
import models.Track;
import play.data.Form;
import play.libs.WS;
import play.libs.F;
import play.mvc.*;


import views.html.*;

import java.util.Map;

import static play.libs.WS.*;

public class Application extends Controller {


    static Form<ShareRecord> shareForm = Form.form(ShareRecord.class);


    public static Result index(String source, String id) {
          if((source != null) && id != null){
              final ShareRecord record = new ShareRecord();
              record.id = id;
              record.source = source.toUpperCase();
              shareForm = shareForm.fill(record);
          }

        return ok(index.render(shareForm,""));


    }
    public static Result submit(){

        final ShareRecord shareRecord = shareForm.bindFromRequest().get();
        return share(shareRecord.source, shareRecord.type, shareRecord.id, shareRecord.destination);

    }

    public static Result share(String source, final String type, String id, String destination) {

        final Provider sourceProvider = Provider.valueOf(source);
        final Provider destinationProvider = Provider.valueOf(destination);
        final Query lookupQuery = sourceProvider.buildLookupQuery(type, id);
        
        final WSRequestHolder lookupRequest = url(lookupQuery.url);
        
        for (Map.Entry<String, String> parameter : lookupQuery.queryParameters.entrySet()) {
        
            lookupRequest.setQueryParameter(parameter.getKey(), parameter.getValue());
         }
        return (async(lookupRequest.get().flatMap(new F.Function<WS.Response, F.Promise<Result>>() {
            public F.Promise<Result> apply(WS.Response response) throws Throwable {

                final Track track = sourceProvider.getTrack(response.asJson());

                final Query searchQuery = destinationProvider.buildSearchQuery(track);
                WSRequestHolder searchRequest = url(searchQuery.url);
                for (Map.Entry<String, String> parameter : searchQuery.queryParameters.entrySet()) {

                    searchRequest.setQueryParameter(parameter.getKey(), parameter.getValue());
                }
                return searchRequest.get().map(new F.Function<Response, Result>() {
                    @Override
                    public Result apply(Response response) throws Throwable {


                        String trackUrl = destinationProvider.getTrackUrl(response.asJson(), track);
                        if(trackUrl == null)
                            return notFound();
                        return ok(index.render(shareForm, trackUrl));
                    }
                });
            }
        })));


    }




}
