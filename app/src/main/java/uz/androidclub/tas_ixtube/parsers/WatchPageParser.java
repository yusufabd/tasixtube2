package uz.androidclub.tas_ixtube.parsers;

import uz.androidclub.tas_ixtube.models.Video;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by yusufabd on 2/22/2017.
 */

public class WatchPageParser {
    public Video getVideoInfo(Document document){
        Video video = new Video();
        Element divDesc = document.select("div.desc").first();
        Elements p = divDesc.select("p");
        StringBuilder descriptionBuilder = new StringBuilder();
        for (int i = 1; i < p.size()-1; i++) {
            descriptionBuilder.append(p.get(i).text().concat("\n"));
        }
        String desc = descriptionBuilder.toString();
        Element pTags = divDesc.select("p").last();
        Elements aTags = pTags.select("a");
        String[] tags = new String[aTags.size()];
        for (int i = 0; i < aTags.size(); i++) {
            tags[i] = aTags.get(i).text();
        }
        video.setAuthor(document.select("div.owner").first().select("span.fl").first().select("b").first().select("a").first().text());
        video.setTags(tags);
        video.setDescription(desc);
        video.setSimilarVideoList(getSimilarVideos(document));
        return video;
    }

    private ArrayList<Video> getSimilarVideos(Document document){
        ArrayList<Video> videoList = new ArrayList<>();
        Element divList = document.select("div.video-list").first();
        Elements divVideo = divList.select("div.video");
        for (Element e :
                divVideo) {
            videoList.add(getOneVideo(e));
        }
        return videoList;
    }

    private Video getOneVideo(Element element){
        String title = element.select("a").attr("title");
        String views =  element.select("div.info2").select("p.views").text();
        String length = element.select("a").select("span.length").text();
        String id = element.select("a").attr("href").substring(7).replace("/", "");
        return new Video(title, "", views, length, id);
    }


}
