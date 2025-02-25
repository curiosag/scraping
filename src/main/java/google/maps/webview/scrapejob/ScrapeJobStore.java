package google.maps.webview.scrapejob;

import google.maps.Point;

import java.util.Optional;

public interface ScrapeJobStore {

    Optional<ScrapeJob> getNext();

    void setProgress(int jobId, Point currentPosition, double pctLatitudeDone);

    void releaseJob(int id, boolean done, String error);
}
