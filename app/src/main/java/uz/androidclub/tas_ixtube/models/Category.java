package uz.androidclub.tas_ixtube.models;

import android.content.Context;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.Divisions;
import uz.androidclub.tas_ixtube.utils.Sections;

/**
 * Created by yusufabd on 2/20/2017.
 */

public class Category implements Constants{

    private String mTitle, mLink;
    private int mId;
    private Divisions mType;
    private Sections mSection;

    public static Category newInstance(Context c, int navId){
        switch (navId){
            case 0:
                return new Category(c.getString(R.string.genre_main), null, -1, Divisions.MAIN, null);
            case 1:
                return new Category(c.getString(R.string.genre_latest), URL_LATEST, -1, Divisions.LATEST, null);
            case 2:
                return new Category(c.getString(R.string.genre_interesting), URL_INTERESTING, -1, Divisions.INTERESTING, null);
            case 3:
                return new Category(c.getString(R.string.genre_humor), URL_HUMOR, humor_id, Divisions.HUMOR, Sections.TOP_DAY);
            case 4:
                return new Category(c.getString(R.string.genre_cinema), URL_CINEMA, cinema_id, Divisions.CINEMA, Sections.TOP_DAY);
            case 5:
                return new Category(c.getString(R.string.genre_music), URL_MUSIC, music_id, Divisions.MUSIC, Sections.TOP_DAY);
            case 6:
                return new Category(c.getString(R.string.genre_sports), URL_SPORT, sport_id, Divisions.SPORT, Sections.TOP_DAY);
            case 7:
                return new Category(c.getString(R.string.genre_tv), URL_TV, tv_id, Divisions.TV, Sections.TOP_DAY);
            case 8:
                return new Category(c.getString(R.string.genre_games), URL_GAME, games_id, Divisions.GAMES, Sections.TOP_DAY);
            case 9:
                return new Category(c.getString(R.string.genre_education), URL_EDUCATION, education_id, Divisions.EDUCATION, Sections.TOP_DAY);
            case 10:
                return new Category(c.getString(R.string.genre_family), URL_FAMILY, family_id, Divisions.FAMILY, Sections.TOP_DAY);
            case 11:
                return new Category(c.getString(R.string.genre_anime), URL_ANIME, anime_id, Divisions.ANIME, Sections.TOP_DAY);
            case 12:
                return new Category(c.getString(R.string.genre_animals), URL_ANIMALS, animals_id, Divisions.ANIMALS, Sections.TOP_DAY);
            case 13:
                return new Category(c.getString(R.string.genre_auto), URL_AUTO, auto_id, Divisions.AUTO, Sections.TOP_DAY);
            case 14:
                return new Category(c.getString(R.string.genre_beauty), URL_BEAUTY, beauty_id, Divisions.BEAUTY, Sections.TOP_DAY);
            case 15:
                return new Category(c.getString(R.string.genre_mix), URL_MISC, misc_id, Divisions.MISC, Sections.TOP_DAY);
            case 16:
                return new Category(c.getString(R.string.genre_news), URL_NEWS, news_id, Divisions.NEWS, Sections.TOP_DAY);
            case 17:
                return new Category(c.getString(R.string.genre_nature), URL_NATURE, nature_id, Divisions.NATURE, Sections.TOP_DAY);
            case 18:
                return new Category(c.getString(R.string.genre_hi_tech), URL_TECH, tech_id, Divisions.TECH, Sections.TOP_DAY);
            case 19:
                return new Category(c.getString(R.string.genre_arts), URL_ARTS, arts_id, Divisions.ARTS, Sections.TOP_DAY);
            case 20:
                return new Category(c.getString(R.string.genre_food), URL_COOK, cook_id, Divisions.COOK, Sections.TOP_DAY);
            case 21:
                return new Category(c.getString(R.string.genre_ads), URL_ADS, ads_id, Divisions.ADS, Sections.TOP_DAY);
        }
        return null;
    }

    public Category() {
    }

    public Category(String mTitle, String mLink, int mId, Divisions mType, Sections mSection) {
        this.mTitle = mTitle;
        this.mLink = mLink;
        this.mId = mId;
        this.mType = mType;
        this.mSection = mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public int getId() {
        return mId;
    }

    public Divisions getType() {
        return mType;
    }

    public void setSection(Sections mSection) {
        this.mSection = mSection;
    }

    public Sections getSection() {
        return mSection;
    }

    public String getUrl(){
        if (mType == Divisions.MAIN){
            return MAIN_PAGE;
        }else if(mType == Divisions.LATEST){
            return MAIN_PAGE + "video/" + mLink + "/?page=";
        }else if(mType == Divisions.INTERESTING){
            return MAIN_PAGE + "video/" + mLink + "/?page=";
        }else {
            switch (mSection){
                case TOP_DAY:
                    return MAIN_PAGE + "top/day?cat=" + mId + "&page=";
                case TOP_THREE_DAYS:
                    return MAIN_PAGE + "top/three?cat=" + mId + "&page=";
                case TOP_WEEK:
                    return MAIN_PAGE + "top/week?cat=" + mId + "&page=";
                case TOP_MONTH:
                    return MAIN_PAGE + "top/month?cat=" + mId + "&page=";
                case RECOMMENDED:
                    return MAIN_PAGE + "video/recommend?cat=" + mId + "&page=";
                case INTERESTING:
                    return MAIN_PAGE + "video/" + mLink + "/interesting/" + "/?page=";
                case NEW:
                    return MAIN_PAGE + "video/" + mLink + "/latest/" + "/?page=";
            }
        }
        return null;
    }
}
