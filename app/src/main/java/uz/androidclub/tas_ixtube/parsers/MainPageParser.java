package uz.androidclub.tas_ixtube.parsers;

import uz.androidclub.tas_ixtube.models.Video;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by yusufabd on 2/18/2017.
 */

public class MainPageParser {

    private Document document;

    public ArrayList<Video> getList(Document document){
        this.document = document;
        ArrayList<Video> list = new ArrayList<>();
        for (Video v :
                getRecommendedVideos()) {
            list.add(v);
        }
        for (Video v :
                getPopularVideos()) {
            list.add(v);
        }

        for (Video v :
                getInterestingVideos()) {
            list.add(v);
        }
        return list;
    }

    private ArrayList<Video> getRecommendedVideos(){
        ArrayList<Video> list = new ArrayList<>();
        Element divHomeRecommended = document.select("div#home-recommended").first();
        Element mainVideo = divHomeRecommended.select("div.video").first();
        list.add(getOneVideo(mainVideo, true));
        Elements videos = divHomeRecommended.select("div.video-list").first().select("div.video");
        for (Element e : videos) {
            list.add(getOneVideo(e, false));
        }
        return list;
    }

    private ArrayList<Video> getPopularVideos(){
        ArrayList<Video> list = new ArrayList<>();
        Element divHomePopular = document.select("div#home-popular").first();
        Elements videos = divHomePopular.select("div.video-list").first().select("div#place_top_video").first().select("div.video");
        for (Element e : videos) {
            list.add(getOneVideo(e, false));
        }
        return list;
    }

    private ArrayList<Video> getInterestingVideos(){
        ArrayList<Video> list = new ArrayList<>();
        Element divHomePopular = document.select("div.list-box").first();
        Elements videos = divHomePopular.select("div.video-list").first().select("div.video");
        for (Element e : videos) {
            list.add(getOneVideo(e, false));
        }
        return list;
    }

    private Video getOneVideo(Element element, boolean isRec){
        String title = element.select("a").attr("title");
        String author = element.select("div.info").select("p.owner").select("a").text();
        String views = "";
        if (isRec)
            views = element.select("div.info").select("p.owner").text();
        else
            views = element.select("div.info").select("p.views").text();
        String length = element.select("a").select("span.length").text();
        String id = element.select("a").attr("href").substring(7).replace("/", "");
        return new Video(title, author, views, length, id);
    }
}