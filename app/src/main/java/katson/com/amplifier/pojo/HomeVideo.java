package katson.com.amplifier.pojo;

/**
 * Created by user on 4/2/2017.
 */

public class HomeVideo {
    private String title;
    private String thumbnail_url;
    private String videoID;
    private String Date;
    private String Description;
    private String DownloadUrl;

    public HomeVideo(String title, String thumbnail_url, String videoid, String Date, String Description, String DownloadUrl) {
        this.title = title;
        this.thumbnail_url= thumbnail_url;
        this.videoID = videoid;
        this.Date = Date;
        this.Description = Description;
        this.DownloadUrl = DownloadUrl;
    }

    public String getTitle() {
        return title;
    }
    public String getThumbnailUrl() {
        return thumbnail_url;
    }
    public String getVideoID() {
        return videoID;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return Description;
    }
    public String getDownloadUrl() {
        return DownloadUrl;
    }
}
