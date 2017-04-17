package uz.androidclub.tas_ixtube.presenter;

/**
 * Created by yusufabd on 2/28/2017.
 */

public class FiltersPresenter {

    String category = "&category=";
    String order = "&order_by=";
    String period = "&period=";
    String length = "&duration=";

    public String getFilteredQuery(int categoryPos, int sortPos, int datePos, int lengthPos){
        StringBuilder filter = new StringBuilder("");
        filter.append(category).append(categoryPos);
        switch (sortPos){
            case 0:
                filter.append(order).append("relevance");
                break;
            case 1:
                filter.append(order).append("upload_date");
                break;
            case 2:
                filter.append(order).append("views");
                break;
            default:
                filter.append(order).append("relevance");
        }

        switch (datePos){
            case 0:
                filter.append(period).append("any");
                break;
            case 1:
                filter.append(period).append("today");
                break;
            case 2:
                filter.append(period).append("week");
                break;
            case 3:
                filter.append(period).append("month");
                break;
            case 4:
                filter.append(period).append("year");
                break;
            default:
                filter.append(period).append("any");
        }

        switch (lengthPos){
            case 0:
                filter.append(length).append("any");
                break;
            case 1:
                filter.append(length).append("short");
                break;
            case 2:
                filter.append(length).append("medium");
                break;
            case 3:
                filter.append(length).append("long");
                break;
            default:
                filter.append(length).append("any");
        }

        return filter.toString();
    }

}
