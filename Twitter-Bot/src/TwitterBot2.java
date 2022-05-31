import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterBot2 {

    public static void main(String[] args) throws TwitterException {

        // Problem 1
        // Create and set a String called message here

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        List<Status> stats = new ArrayList<>();
        List<Status> stats2 = new ArrayList<>();
        ArrayList<String> fastFood = new ArrayList<>();

        fastFood.add("SUBWAY");
        fastFood.add("Starbucks");
        fastFood.add("dunkindonuts");
        fastFood.add("pizzahut");
        fastFood.add("BurgerKing");
        fastFood.add("Wendys");
        fastFood.add("tacobell");
        fastFood.add("dominos");
        fastFood.add("kfc");
        fastFood.add("sonicdrivein");
        fastFood.add("PandaExpress");
        fastFood.add("PapaJohns");
        fastFood.add("McDonalds");
        fastFood.add("Arbys");
        fastFood.add("PopeyesChicken");

        while (fastFood.size() != 0) {
            List<Status> methodList = pageGang(fastFood, twitter);
            fastFood.remove(0);
            stats.addAll(methodList);
            //methodList.clear();
        }

        //Array of strings (names of twitter handles)
        //For loop for every element in array (names) pass into addAll page bs
        //

        while (stats.size() > 0) {
            Status temp = stats.remove(0);
            String txt = temp.getText().toLowerCase();
            if (!temp.isRetweet() && temp.getText().charAt(0) != '@') {
                if ((temp.getText().contains("new")
                        || txt.contains("now avaiable")
                        || txt.contains("limited time")
                        || txt.contains("introducing") || txt.contains("soon")
                        || txt.contains("is back")
                        || txt.contains("are back"))) {
                    if (!txt.contains("win") && !txt.contains("students")
                            && !txt.contains("terms")
                            && !txt.contains("condition")
                            && !txt.contains("shirt") && !txt.contains("hoodie")
                            && !txt.contains("hat") && !txt.contains("chicago")
                            && !txt.contains("los angeles")
                            && !txt.contains("atl") && !txt.contains("shop")) {
                        stats2.add(temp);
                        //System.out.println("\n" + temp.getText());
                        //System.out.println("\n" + temp.getCreatedAt());
                    }

                }

            }

        }

        Status[] fsArray = new Status[stats2.size()];

        for (int i = 0; i < stats2.size(); i++) {
            int laterCount = 0;
            Date d = stats2.get(i).getCreatedAt();
            Status el = stats2.get(i);
            for (int j = 0; j < stats2.size(); j++) {
                Date d2 = stats2.get(j).getCreatedAt();
                if (d.after(d2)) {
                    laterCount++;
                }
            }
            fsArray[laterCount] = el;
        }

        for (int i = 0; i < fsArray.length; i++) {
            Status first = fsArray[i];
            Long id = first.getId();
            twitter.retweetStatus(id);
        }
    }

    public static List<Status> pageGang(ArrayList<String> fastFood,
            Twitter twitter) {
        List<Status> stats = new ArrayList<>();

        int pageno = 1;
        while (true) {
            try {
                System.out.println("getting tweets");
                int size = stats.size(); // actual tweets count we got
                Paging page = new Paging(pageno, 200);
                stats.addAll(twitter.getUserTimeline(fastFood.get(0), page));
                System.out.println("total got : " + stats.size());
                if (stats.size() == size) {
                    break;
                } // we did not get new tweets so we have done the job
                pageno++;
                //TimeUnit.SECONDS.sleep(1);
            } catch (TwitterException e) {
                System.out.println(e.getErrorMessage());
            }
        }

        return stats;

    }

}
