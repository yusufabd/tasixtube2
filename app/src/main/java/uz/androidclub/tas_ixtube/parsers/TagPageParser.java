package uz.androidclub.tas_ixtube.parsers;

import uz.androidclub.tas_ixtube.models.Video;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by yusufabd on 3/8/2017.
 */

public class TagPageParser {

    public ArrayList<Video> getList(Document document){
        ArrayList<Video> list = new ArrayList<>();
        Element divRecommended = document.select("div.video-recommended").first();
        if (divRecommended != null) {
            for (Element e :
                    divRecommended.select("div.video")) {
                list.add(getOneVideo(e, true));
            }
        }

        Element divList = document.select("div.video-list").first();
        for (Element e :
                divList.select("div.video")) {
            list.add(getOneVideo(e, true));
        }

        return list;
    }


    private Video getOneVideo(Element element, boolean isRec){
        String title = element.select("a").attr("title");
        String author = element.select("div.info").select("p.owner").select("a").text();
        String views = element.select("div.info").select("p.views").text();
        String length = element.select("a").select("span.length").text();
        String id = element.select("a").attr("href").substring(7).replace("/", "");
        return new Video(title, author, views, length, id);
    }
}
