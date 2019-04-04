//import java.util.List;
//
//public class Author
//{
//    private String username;
//
//    private String modified;
//
//    private String link;
//
//    private boolean is_active;
//
//    public void setUsername(String username){
//        this.username = username;
//    }
//    public String getUsername(){
//        return this.username;
//    }
//    public void setModified(String modified){
//        this.modified = modified;
//    }
//    public String getModified(){
//        return this.modified;
//    }
//    public void setLink(String link){
//        this.link = link;
//    }
//    public String getLink(){
//        return this.link;
//    }
//    public void setIs_active(boolean is_active){
//        this.is_active = is_active;
//    }
//    public boolean getIs_active(){
//        return this.is_active;
//    }
//}
//
//public class Articles
//{
//    private int id;
//
//    private String title;
//
//    private String modified;
//
//    private String link;
//
//    public void setId(int id){
//        this.id = id;
//    }
//    public int getId(){
//        return this.id;
//    }
//    public void setTitle(String title){
//        this.title = title;
//    }
//    public String getTitle(){
//        return this.title;
//    }
//    public void setModified(String modified){
//        this.modified = modified;
//    }
//    public String getModified(){
//        return this.modified;
//    }
//    public void setLink(String link){
//        this.link = link;
//    }
//    public String getLink(){
//        return this.link;
//    }
//}
//
//public class Categories
//{
//    private int id;
//
//    private String modified;
//
//    private String name;
//
//    private String link;
//
//    public void setId(int id){
//        this.id = id;
//    }
//    public int getId(){
//        return this.id;
//    }
//    public void setModified(String modified){
//        this.modified = modified;
//    }
//    public String getModified(){
//        return this.modified;
//    }
//    public void setName(String name){
//        this.name = name;
//    }
//    public String getName(){
//        return this.name;
//    }
//    public void setLink(String link){
//        this.link = link;
//    }
//    public String getLink(){
//        return this.link;
//    }
//}
//
//public class Place
//{
//    private int id;
//
//    private Author author;
//
//    private List<String> images;
//
//    private List<Double> coordinates;
//
//    private String link;
//
//    private List<Articles> articles;
//
//    private String title;
//
//    private String modified;
//
//    private List<Categories> categories;
//
//    private String description;
//
//    public void setId(int id){
//        this.id = id;
//    }
//    public int getId(){
//        return this.id;
//    }
//    public void setAuthor(Author author){
//        this.author = author;
//    }
//    public Author getAuthor(){
//        return this.author;
//    }
//    public void setImages(List<String> images){
//        this.images = images;
//    }
//    public List<String> getImages(){
//        return this.images;
//    }
//    public void setCoordinates(List<Double> coordinates){
//        this.coordinates = coordinates;
//    }
//    public List<Double> getCoordinates(){
//        return this.coordinates;
//    }
//    public void setLink(String link){
//        this.link = link;
//    }
//    public String getLink(){
//        return this.link;
//    }
//    public void setArticles(List<Articles> articles){
//        this.articles = articles;
//    }
//    public List<Articles> getArticles(){
//        return this.articles;
//    }
//    public void setTitle(String title){
//        this.title = title;
//    }
//    public String getTitle(){
//        return this.title;
//    }
//    public void setModified(String modified){
//        this.modified = modified;
//    }
//    public String getModified(){
//        return this.modified;
//    }
//    public void setCategories(List<Categories> categories){
//        this.categories = categories;
//    }
//    public List<Categories> getCategories(){
//        return this.categories;
//    }
//    public void setDescription(String description){
//        this.description = description;
//    }
//    public String getDescription(){
//        return this.description;
//    }
//}
//
//public class Root
//{
//    private int status;
//
//    private Place place;
//
//    private String user;
//
//    public void setStatus(int status){
//        this.status = status;
//    }
//    public int getStatus(){
//        return this.status;
//    }
//    public void setPlace(Place place){
//        this.place = place;
//    }
//    public Place getPlace(){
//        return this.place;
//    }
//    public void setUser(String user){
//        this.user = user;
//    }
//    public String getUser(){
//        return this.user;
//    }
//}
