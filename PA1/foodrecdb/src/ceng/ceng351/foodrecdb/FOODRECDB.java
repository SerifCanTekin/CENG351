package ceng.ceng351.foodrecdb;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


public class FOODRECDB implements IFOODRECDB{

    private static String user = "e2448892"; // TODO: Your userName
    private static String password = "K_hjZPZQBNEz2QwZ"; //  TODO: Your password
    private static String host = "momcorp.ceng.metu.edu.tr"; // host name
    private static String database = "db2448892"; // TODO: Your database name
    private static int port = 8080; // port

    private static Connection connection = null;





    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection =  DriverManager.getConnection(url, user, password);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int createTables() {
        int InsertedTables = 0;

        String createMenuItemsTable = "create table if not exists MenuItems(" +
                "itemID int not null," +
                "itemName varchar(40)," +
                "cuisine varchar(20)," +
                "price int," +
                "primary key (itemID))";

        String createIngredientsTable = "create table if not exists Ingredients (" +
                "ingredientID int not null," +
                "ingredientName varchar(40)," +
                "primary key (ingredientID))";

        String createIncludesTable = "create table if not exists Includes (" +
                "itemID int not null," +
                "ingredientID int not null," +
                "primary key (itemID,ingredientID)," +
                "foreign key (itemID) references MenuItems(itemID),"+
                "foreign key (ingredientID) references Ingredients(ingredientID))";
        String createRatingsTable = "create table if not exists Ratings (" +
                "ratingID int not null," +
                "itemID int not null," +
                "rating int," +
                "ratingDate date," +
                "primary key (ratingID)," +
                "foreign key (itemID) references MenuItems(itemID))";
        String createDietaryCategoriesTable = "create table if not exists DietaryCategories (" +
                "ingredientID int not null," +
                "dietaryCategory varchar(20)," +
                "primary key (ingredientID,dietaryCategory)," +
                "foreign key (ingredientID) references Ingredients(ingredientID))";


        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(createMenuItemsTable);
            InsertedTables++;

            statement.executeUpdate(createIngredientsTable);
            InsertedTables++;

            statement.executeUpdate(createIncludesTable);
            InsertedTables++;

            statement.executeUpdate(createRatingsTable);
            InsertedTables++;

            statement.executeUpdate(createDietaryCategoriesTable);
            InsertedTables++;

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

            return InsertedTables;
    }

    @Override
    public int dropTables() {
        int DroppedTables = 0;

        String dropIncludesTable = "drop table if exists Includes;";

        String dropRatingsTable = "drop table if exists Ratings;";

        String dropMenuItemsTable = "drop table if exists MenuItems;";

        String dropDietaryCategoriesTable = "drop table if exists DietaryCategories;";

        String dropIngredientsTable = "drop table if exists Ingredients;";


        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(dropIncludesTable);
            DroppedTables++;

            statement.executeUpdate(dropRatingsTable);
            DroppedTables++;

            statement.executeUpdate(dropMenuItemsTable);
            DroppedTables++;

            statement.executeUpdate(dropDietaryCategoriesTable);
            DroppedTables++;

            statement.executeUpdate(dropIngredientsTable);
            DroppedTables++;

            //close
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DroppedTables;
    }

    @Override
    public int insertMenuItems(MenuItem[] items) {
        int MenuItems_count=0;

        for(int i=0;i<items.length;i++){
            String MenuItems_query = "insert into MenuItems values (\"" +
                    items[i].getItemID() + "\",\"" +
                    items[i].getItemName() + "\",\"" +
                    items[i].getCuisine() + "\",\"" +
                    items[i].getPrice() + "\")";

            try {
                Statement statement = connection.createStatement();

                statement.executeUpdate(MenuItems_query);
                MenuItems_count++;

                statement.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }


        return MenuItems_count;
    }

    @Override
    public int insertIngredients(Ingredient[] ingredients) {
        int Ingredients_count=0;

        for(int i=0;i<ingredients.length;i++){
            String Ingredients_query = "insert into Ingredients values (\"" +
                    ingredients[i].getIngredientID() + "\",\"" +
                    ingredients[i].getIngredientName() + "\")";

            try {
                Statement statement = connection.createStatement();

                statement.executeUpdate(Ingredients_query);
                Ingredients_count++;

                statement.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }


        return Ingredients_count;
    }

    @Override
    public int insertIncludes(Includes[] includes) {
        int Includes_count=0;

        for(int i=0;i<includes.length;i++){
            String MenuItems_query = "insert into Includes values (\"" +
                    includes[i].getItemID() + "\",\"" +
                    includes[i].getIngredientID() + "\")";

            try {
                Statement statement = connection.createStatement();

                statement.executeUpdate(MenuItems_query);
                Includes_count++;

                statement.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }


        return Includes_count;
    }

    @Override
    public int insertDietaryCategories(DietaryCategory[] categories) {
        int DietaryCategories_count=0;

        for(int i=0;i<categories.length;i++){
            String MenuItems_query = "insert into DietaryCategories values (\"" +
                    categories[i].getIngredientID() + "\",\"" +
                    categories[i].getDietaryCategory() + "\")";

            try {
                Statement statement = connection.createStatement();

                statement.executeUpdate(MenuItems_query);
                DietaryCategories_count++;

                statement.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }


        return DietaryCategories_count;
    }

    @Override
    public int insertRatings(Rating[] ratings) {
        int Ratings_count=0;

        for(int i=0;i<ratings.length;i++){
            String Ratings_query = "insert into Ratings values (\"" +
                    ratings[i].getRatingID() + "\",\"" +
                    ratings[i].getItemID() + "\",\"" +
                    ratings[i].getRating() + "\",\"" +
                    ratings[i].getRatingDate() + "\")";

            try {
                Statement statement = connection.createStatement();

                statement.executeUpdate(Ratings_query);
                Ratings_count++;

                statement.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        return Ratings_count;
    }

    @Override
    public MenuItem[] getMenuItemsWithGivenIngredient(String name) {
        ArrayList<MenuItem> items_list = new ArrayList<MenuItem>();
        MenuItem items[];
        MenuItem items_new;
        ResultSet res;

        String query  = "SELECT distinct m1.itemID, m1.itemName, m1.cuisine, m1.price " +
                "FROM MenuItems m1, Includes i1, Ingredients i2 " +
                "WHERE m1.itemID = i1.itemID AND i2.ingredientID = i1.ingredientID AND i2.ingredientName = '" + name + "' " +
                "order by m1.itemID;";

        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                int m1_item_ID = res.getInt("itemID");
                String m1_item_Name = res.getString("itemName");
                String m1_cuisine_ = res.getString("cuisine");
                int m1_price_ = res.getInt("price");
                items_new = new MenuItem(m1_item_ID,m1_item_Name,m1_cuisine_,m1_price_);
                items_list.add(items_new);

            }


            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        items = new MenuItem[items_list.size()];
        items = items_list.toArray(items);


        return items;
    }

    @Override
    public MenuItem[] getMenuItemsWithoutAnyIngredient() {
        ArrayList<MenuItem> items_list = new ArrayList<MenuItem>();
        MenuItem items[];
        MenuItem items_new;
        ResultSet res;


        String query  = "SELECT distinct m1.itemID, m1.itemName, m1.cuisine, m1.price " +
                "FROM MenuItems m1 " +
                "WHERE m1.itemID NOT IN ( SELECT i1.itemID FROM Includes i1) " +
                "order by m1.itemID;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                int m1_item_ID = res.getInt("itemID");
                String m1_item_Name = res.getString("itemName");
                String m1_cuisine_ = res.getString("cuisine");
                int m1_price_ = res.getInt("price");
                items_new = new MenuItem(m1_item_ID,m1_item_Name,m1_cuisine_,m1_price_);
                items_list.add(items_new);

            }


            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        items = new MenuItem[items_list.size()];
        items = items_list.toArray(items);


        return items;
    }

    @Override
    public Ingredient[] getNotIncludedIngredients() {
        ArrayList<Ingredient> ings_list = new ArrayList<Ingredient>();
        Ingredient ings[];
        Ingredient ings_new;
        ResultSet res;


        String query  = "SELECT distinct i1.ingredientID, i1.ingredientName " +
                "FROM Ingredients i1 " +
                "WHERE i1.ingredientID NOT IN ( SELECT i2.ingredientID FROM Includes i2) " +
                "order by i1.ingredientID;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                int i1_ingredient_ID = res.getInt("ingredientID");
                String i1_ingredient_Name = res.getString("ingredientName");
                ings_new = new Ingredient(i1_ingredient_ID,i1_ingredient_Name);
                ings_list.add(ings_new);

            }


            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ings = new Ingredient[ings_list.size()];
        ings = ings_list.toArray(ings);


        return ings;
    }

    @Override
    public MenuItem getMenuItemWithMostIngredients() {

        MenuItem item = null;
        ResultSet res;


        String query  = "SELECT distinct m1.itemID, m1.itemName, m1.cuisine, m1.price " +
                "FROM MenuItems m1 " +
                "WHERE m1.itemID IN ( SELECT i1.itemID FROM Includes i1 GROUP BY i1.itemID HAVING COUNT(*) >= ALL (SELECT COUNT(*) FROM Includes i2 GROUP BY i2.itemID)) " +
                "order by m1.itemID;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);
            res.next();
            int m1_item_ID = res.getInt("itemID");
            String m1_item_Name = res.getString("itemName");
            String m1_cuisine_ = res.getString("cuisine");
            int m1_price_ = res.getInt("price");
            item = new MenuItem(m1_item_ID,m1_item_Name,m1_cuisine_,m1_price_);

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return item;
    }

    @Override
    public QueryResult.MenuItemAverageRatingResult[] getMenuItemsWithAvgRatings() {
        ArrayList<QueryResult.MenuItemAverageRatingResult> items_list = new ArrayList<QueryResult.MenuItemAverageRatingResult>();
        QueryResult.MenuItemAverageRatingResult items[];
        QueryResult.MenuItemAverageRatingResult items_new;
        ResultSet res;


        String query  = "SELECT distinct m1.itemID, m1.itemName, AVG(r.rating) AS avgRating " +
                "FROM MenuItems m1, Ratings r " +
                "WHERE m1.itemID = r.itemID " +
                "GROUP BY r.itemID " +
                "UNION " +
                "SELECT m2.itemID, m2.itemName, NULL " +
                "FROM MenuItems m2 " +
                "WHERE m2.itemID NOT IN(SELECT r1.itemID FROM Ratings r1) " +
                "order by 3 DESC;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                String m1_item_ID = res.getString("itemID");
                String m1_item_Name = res.getString("itemName");
                String m1_rating = res.getString("avgRating");
                items_new = new QueryResult.MenuItemAverageRatingResult(m1_item_ID,m1_item_Name,m1_rating);
                items_list.add(items_new);

            }


            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        items = new QueryResult.MenuItemAverageRatingResult[items_list.size()];
        items = items_list.toArray(items);


        return items;
    }

    @Override
    public MenuItem[] getMenuItemsForDietaryCategory(String category) {
        ArrayList<MenuItem> items_list = new ArrayList<MenuItem>();
        MenuItem items[];
        MenuItem items_new;
        ResultSet res;
// = '" + name + "' "
        String query  = "SELECT distinct * " +
                "FROM MenuItems m " +
                "WHERE m.itemID NOT IN " +
                "( SELECT i.itemID " +
                "FROM Includes i, (SELECT d.ingredientID FROM DietaryCategories d WHERE d.ingredientID NOT IN (SELECT d1.ingredientID FROM DietaryCategories d1 WHERE d1.dietaryCategory = '" + category + "' )) AS Temp " +
                "WHERE i.ingredientID = Temp.ingredientID ) " +
                "AND m.itemID NOT IN(SELECT m2.itemID FROM MenuItems m2 WHERE m2.itemID NOT IN(SELECT i3.itemID FROM Includes i3)) " +
                "order by 1;";

        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                int m1_item_ID = res.getInt("itemID");
                String m1_item_Name = res.getString("itemName");
                String m1_cuisine_ = res.getString("cuisine");
                int m1_price_ = res.getInt("price");
                items_new = new MenuItem(m1_item_ID,m1_item_Name,m1_cuisine_,m1_price_);
                items_list.add(items_new);

            }


            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        items = new MenuItem[items_list.size()];
        items = items_list.toArray(items);


        return items;
    }

    @Override
    public Ingredient getMostUsedIngredient() {

        Ingredient ing = null;
        ResultSet res;


        String query  = "SELECT distinct i.ingredientID, i.ingredientName " +
                "FROM Ingredients i " +
                "WHERE i.ingredientID IN ( SELECT i1.ingredientID FROM Includes i1 GROUP BY i1.ingredientID HAVING COUNT(*) >= ALL (SELECT COUNT(*) FROM Includes i2 GROUP BY i2.ingredientID)) " +
                "order by i.ingredientID;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);
            res.next();
            int i1_ingredient_ID = res.getInt("ingredientID");
            String i1_ingredient_Name = res.getString("ingredientName");
            ing = new Ingredient(i1_ingredient_ID,i1_ingredient_Name);

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return ing;
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgRating() {
        ArrayList<QueryResult.CuisineWithAverageResult> items_list = new ArrayList<QueryResult.CuisineWithAverageResult>();
        QueryResult.CuisineWithAverageResult items[];
        QueryResult.CuisineWithAverageResult items_new;
        ResultSet res;


        String query  = "SELECT distinct m1.cuisine, AVG(r.rating) AS avgRating " +
                "FROM MenuItems m1, Ratings r " +
                "WHERE m1.itemID = r.itemID " +
                "GROUP BY m1.cuisine " +
                "UNION " +
                "SELECT distinct m2.cuisine, NULL " +
                "FROM MenuItems m2 " +
                "WHERE m2.cuisine NOT IN (SELECT m3.cuisine FROM Ratings r1, MenuItems m3 WHERE m3.itemID = r1.itemID ) " +
                "order by 2 DESC;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                String m1_cuisine = res.getString("cuisine");
                String cuisine_rating = res.getString("avgRating");
                items_new = new QueryResult.CuisineWithAverageResult(m1_cuisine,cuisine_rating);
                items_list.add(items_new);

            }


            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        items = new QueryResult.CuisineWithAverageResult[items_list.size()];
        items = items_list.toArray(items);


        return items;
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgIngredientCount() {
        ArrayList<QueryResult.CuisineWithAverageResult> items_list = new ArrayList<QueryResult.CuisineWithAverageResult>();
        QueryResult.CuisineWithAverageResult items[];
        QueryResult.CuisineWithAverageResult items_new;
        ResultSet res;


        String query  = "SELECT distinct m1.cuisine, COUNT(inc.ingredientID)/(COUNT(distinct m2.itemID)*COUNT(distinct m2.itemID)) AS avgIngredient " +
                "FROM MenuItems m1, Includes inc,MenuItems m2 " +
                "WHERE m1.itemID = inc.itemID AND m1.cuisine = m2.cuisine " +
                "GROUP BY m1.cuisine " +
                "UNION " +
                "SELECT distinct m3.cuisine, 0 " +
                "FROM MenuItems m3 " +
                "WHERE m3.cuisine NOT IN (SELECT distinct m4.cuisine FROM Includes inc2, MenuItems m4 WHERE m4.itemID = inc2.itemID ) " +
                "order by 2 DESC;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                String m1_cuisine = res.getString("cuisine");
                String avg_ingredient = res.getString("avgIngredient");
                items_new = new QueryResult.CuisineWithAverageResult(m1_cuisine,avg_ingredient);
                items_list.add(items_new);

            }


            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        items = new QueryResult.CuisineWithAverageResult[items_list.size()];
        items = items_list.toArray(items);


        return items;
    }

    @Override
    public int increasePrice(String ingredientName, String increaseAmount) {


        int numberofTablesIncreased = 0;


        //Player(number:integer, teamname:char(20), name:char(30), age:integer, position:char(3))
        String query = "UPDATE MenuItems m " +
                "SET m.price = m.price + '" + increaseAmount + "'  " +
                "WHERE m.itemID IN(SELECT inc.itemID FROM Includes inc, Ingredients ing WHERE ing.ingredientID = inc.ingredientID AND ing.ingredientName = '" + ingredientName + "') ;";

        try {
            Statement statement = connection.createStatement();

            numberofTablesIncreased = statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberofTablesIncreased;
    }

    @Override
    public Rating[] deleteOlderRatings(String date) {
        ArrayList<Rating> ratings_list = new ArrayList<Rating>();
        Rating rats[];
        Rating rats_new;
        ResultSet res;


        String query = "SELECT * " +
                "FROM Ratings r " +
                "WHERE r.ratingDate <  '" + date + "' ;";
        try{
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);


            while(res.next()){
                int r_ratingID = res.getInt("ratingID");
                int r_itemID = res.getInt("itemID");
                int r_rating = res.getInt("rating");
                String r_ratingDate = res.getString("ratingDate");

                rats_new = new Rating(r_ratingID,r_itemID,r_rating,r_ratingDate);
                ratings_list.add(rats_new);

            }
//"WHERE m1.itemID = i1.itemID AND i2.ingredientID = i1.ingredientID AND i2.ingredientName = '" + name + "' ;"; +

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        rats = new Rating[ratings_list.size()];
        rats = ratings_list.toArray(rats);




        String query1 = "DELETE FROM Ratings r2 "+
                "WHERE r2.ratingDate < '" + date + "' ;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(query1);

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        return rats;
    }
}
