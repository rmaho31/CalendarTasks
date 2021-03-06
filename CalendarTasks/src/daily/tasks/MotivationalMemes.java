package daily.tasks;

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;
import java.io.IOException;
import java.net.URL;

public class MotivationalMemes {
	
		

    //browser user agent to reduce 403 errors
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
        
    public static URL generateMemes() throws IOException{
    	
    	Pattern pat = Pattern.compile("http.*?.(?:jpg)|http.*?.(?:jpeg)|http.*?.(?:gif)|http.*?.(?:png)|http.*?.(?:tif)");
        ArrayList<String> resultUrls = new ArrayList<String>();
        
    	//Terms to be randomly selected for search query
    	ArrayList<String> term1 = new ArrayList<String>();
    	term1.add("eloquent"); term1.add("emotional");	term1.add("expressive"); term1.add("gripping");	term1.add("heartbreaking");	term1.add("heartrending"); term1.add("inspirational"); term1.add("inspiring");
    	term1.add("meaningful"); term1.add("persuasive"); term1.add("poignant"); term1.add("stirring");	term1.add("stunning"); term1.add("touching"); term1.add("arousing"); term1.add("awakening"); term1.add("impelling");
    	term1.add("motivating"); term1.add("propelling"); term1.add("provoking"); term1.add("quickening"); term1.add("rallying"); term1.add("rousing");	term1.add("stimulating"); term1.add("affective"); term1.add("breathless");
    	term1.add("dynamic"); term1.add("emotive");	term1.add("facund"); term1.add("far-out"); term1.add("hairy");	term1.add("impressive"); term1.add("mind-bending");	term1.add("mind-blowing");
    	term1.add("sententious"); term1.add("significant");	term1.add("something");	term1.add("stimulative");
    	
        //Fetch the page
        final Document doc = Jsoup.connect("https://www.google.com/search?q=" + term1.get(getRandomInts(0,term1.size()-1)) + "+memes/images&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjn-fjhvYHdAhUr7YMKHWI2COQQ_AUICigB&biw=1210&bih=1333")
        		.referrer("www.google.com").userAgent(USER_AGENT).get();
        
        /*Grabs the elements of the div with class rg_meta as this is where the links to the images are located
         * regex pattern that gets the job done to extract the URL from each element and stores it in an arrayList.
         */
        Elements elements = doc.select("div.rg_meta");
        
            
        for (Element element : elements) {
            if (element.childNodeSize() > 0) {
                String a = element.childNode(0).toString();
                Matcher matcher = pat.matcher(a);                
                if (matcher.find()) {
                	resultUrls.add(matcher.group(0));
                }
            }
        }
        
        // randomly selects an index of the result arrayList and then returns it
        URL urlInput = new URL(resultUrls.get(getRandomInts(0,resultUrls.size()-1)));
        
        return urlInput;
    } 
        
    public static String generateNatureNews() throws IOException{
        	
      	
        String a;
        ArrayList<String> resultUrls = new ArrayList<String>();
        String bodyText;
        //Fetch the page
        Document doc = Jsoup.connect("https://www.nature.com/nature/articles?type=news").userAgent(USER_AGENT).get();
            
        /*
        * Grabs the h3 header class mb10 and the anchor following it
        */
        Elements elements = doc.select("h3.mb10 > a");
        
        //grabs the href attr from the anchor, appends to the parent website and adds to the arrayList
        for (Element element : elements) {
            if (element.childNodeSize() > 0) {
                a = element.attr("href");
                a = "https://www.nature.com" + a;
                resultUrls.add(a);
            }
        }   
        
        // randomly selects an index of the result arrayList and then returns it
        
        doc = Jsoup.connect(resultUrls.get(getRandomInts(0,resultUrls.size()-1))).userAgent(USER_AGENT).get();
        bodyText = doc.select("div.article__body > p").toString();
        
        return bodyText;
    } 
    	
    
    //generates random numbers for our arrayLists
    public static int getRandomInts(int min, int max){
    	Random random = new Random();
    	return random.ints(min,(max+1)).findFirst().getAsInt();
    }
}
      