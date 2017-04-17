package uz.androidclub.tas_ixtube.parsers;

import uz.androidclub.tas_ixtube.models.Video;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by yusufabd on 2/19/2017.
 */

public class RegularParser {

    public ArrayList<Video> getList(Document document){
        ArrayList<Video> list = new ArrayList<>();
        Elements videoList = document.select("div.video-list").first().select("div.video");
        for (Element e :
                videoList) {
            list.add(getOneVideo(e));
        }
        return list;
    }

    private Video getOneVideo(Element element){
        String title = element.select("a").attr("title");
        String author = element.select("div.info").select("p.owner").select("a").text();
        String views = element.select("div.info").select("p.views").text();
        String length = element.select("a").select("span.length").text();
        String id = element.select("a").attr("href").substring(7).replace("/", "");
        return new Video(title, author, views, length, id);
    }
}
