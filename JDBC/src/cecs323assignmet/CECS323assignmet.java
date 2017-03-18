/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs323assignmet;

import java.util.*;
import java.sql.*;


public class CECS323assignmet {

    //Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    public static Scanner in = new Scanner(System.in);
    
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/adriantestdb2;user=adrian;password=adrian";
/**1
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    
    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        
        
        //Constructing the database URL connection string
        DB_URL = DB_URL;//+ "Lab#5"+ ";user="+";password=";
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            // Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            
            boolean run = true; 
            //An finite while loop to display the menus
            while(run == true)
            {
                try{
                 //making the thread sleep to delay the display
                Thread.sleep(1000);
                //making a table for the user to chose
                System.out.println("\b\b\b\b\b\b\b\b\b\b\b\b");
                System.out.println("\nChoose one of the following:"
                        + "\n1. List all writing groups.\n"
                        + "2. List all the data for a group specified by the user.\n"
                        + "3. List all publishers.\n"
                        + "4. List all the data for a pubisher specified by the user.\n"
                        + "5. List all book titles.\n" 
                        + "6. List all the data for a book specified by the user.\n"
                        + "7. Insert a new book.\n"
                        + "8. Insert a new publisher and update all book published by one publisher to be published by the new pubisher. \n"
                        + "9. Remove a book specified by the user. \n"
                        + "10. Log out.");
                //choose vrable to recive the user's chose 
                int choose = in.nextInt();
                
                switch(choose)
                {
                    //List all writing groups
                    case 1:
                        System.out.println("\nAll writing groups:");
                        showWritingGroups(stmt);
                        break;
                    //List all the data for a group specified by the user.
                    case 2:
                        showSpecificGroupData(stmt);
                        break;
                     //List all publishers
                    case 3:
                        System.out.println("\nAll Publishers:");                       
                        showPublisher(stmt);
                        break;
                    //List all the data for a pubisher specified by the user
                    case 4:
                        showSpecificPublisherData(stmt);
                        break;
                    //List all book titles  
                    case 5:
                        showBookTitle(stmt);
                        break;
                    //end the connection 
                    case 6:
                        showSpecificBookData(stmt);
                        break;
                     case 7:
                        insertNewBook(stmt);
                        break;                     
                     case 8:
                        insertUpdate(stmt);
                        break;                       
                    case 9:
                        System.out.println("\nRemove a book:");   
                        remove(stmt);
                        break;                        
                    case 10:
                        System.out.println("\nLogging Out...");
                        run = false; //escape the while loop
                        break;                                            
                    //if the user input is not listed this will output. 
                    default:
                        System.out.println("Unavaliable Command");
                        break;
                       
                }
             }catch(InputMismatchException e){
               System.out.println("Input Mismatch\nOnly enter integers"); 
            }
        }
           
            
            
            // Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();

        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                System.out.println("an error occured");
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }
    
    //------------------------------------- Functions ------------------------------------
    /*
    
    showtitle method to display all the titles from the albums table
    
    */
    public static void showWritingGroups(Statement stmt) throws SQLException{
       
        String sql;
        //the sql command 
            sql = "SELECT DISTINCT GroupName FROM WRITINGGROUP";
            ResultSet rs = stmt.executeQuery(sql);
            
            int number = 0;//to list with numbers
            
            while (rs.next()) {
                //Retrieve by column name
                String groupname = rs.getString("GroupName");
                number++;
                
                //Display values
               System.out.println(number+")"+dispNull(groupname));                 
            }
            rs.close();
    }
    /*
     * Showstudio method is for displaying all the studio names from the rstudio table
    */
    public static void showPublisher (Statement stmt) throws SQLException{
       
        String sql;
        //sql command
            sql = "SELECT DISTINCT PublisherName FROM PUBLISHER";
            ResultSet rs = stmt.executeQuery(sql);

            int number = 0;// to list with numbers
            
            while (rs.next()) {
                //Retrieve by column name
                String PublisherName = rs.getString("PublisherName");
                number++;

                //Display values
               System.out.println(number+")"+dispNull(PublisherName));
                 
            }
            rs.close();
    }
    /*
     * showgroup method is for displaying all the group names from the rgroup table
    */
    
    public static void showBookTitle (Statement stmt) throws SQLException{
       
        String sql;
        //sql command 
        sql = "SELECT DISTINCT BOOKTITLE FROM BOOK";
        ResultSet rs = stmt.executeQuery(sql);

        int number = 0;// to be listed with numbers

        while (rs.next()) {
            //Retrieve by column name
            String title = rs.getString("BOOKTITLE");
            number++;

            //Display values
           System.out.println(number+")"+dispNull(title));

        }
        rs.close();// closing connection
    }
    /*
     * showspecific  method is for displaying all the attributes the user chooses from the albums table 
    */
    public static void showSpecificPublisherData(Statement stmt) throws SQLException {
       
       System.out.println("Choose which attrabute to insurt then press p to print.");
       ArrayList attrib = new ArrayList<>(); // an array to register every attabute
       boolean run = true;// while finite varable 
       boolean isinlist;//to check if it is already l
       String listedattrib=" ";// collecting every attrabute
       while(run == true)// start of the finite loo[
       {
           System.out.println("Press p to print\n\n1.Publisher Name\n2.Publisher Address\n3.Publisher Phone\n4.Publisher Mail");
           String chose = in.next();// the user choses what to add 
           switch(chose)// to add the specific attribute
           {
               //to add an attribute
               case "1":
                   //getting the title
                   String Publishername = "Publishername";
                   //checking if it's already listed
                   isinlist = checkiflisted(attrib, Publishername);
                   //if it's not listed then add it to the arraylist
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted Publisher's Name\n");
                        attrib.add(Publishername);//adding it to the array list
                        listedattrib += "Publisher Name: ";// adding it to the title attribute
                   } else {
                       System.out.println("\nalready in the list\n");//not listed
                   }
                 
                   break;
               
               case "2":
                   String Publisheraddress = "Publisheraddress";
                   isinlist = checkiflisted(attrib, Publisheraddress);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted Publisher Address\n");
                        attrib.add(Publisheraddress);
                        listedattrib += "Publisher Address: ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;
                   
               case "3":
                    String Publisherphone = "Publisherphone";
                   isinlist = checkiflisted(attrib, Publisherphone);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted Publisher Phone\n");
                        attrib.add(Publisherphone);
                        listedattrib += "Publisher Phone:  ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;
                   
               case "4":
                   String Publisheremail = "Publisheremail";
                   isinlist = checkiflisted(attrib, Publisheremail);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted Publisher Email\n");
                        attrib.add(Publisheremail);
                        listedattrib += "Publisher Mail:  ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;          
               case "p":
                   //printing the listed attributes
                   System.out.println("\nPrinting chosen attrabutes\n");
                   run = false;
                   
                   break;
               case "P":
                   //printing with a capitial letter
                   System.out.println("\nPrinting chosen attrabutes\n");
                   run = false;
                   
                   break;              
               default:
                   //not a listed command
                   System.out.println("\nNot a listed attrabute\n");
                   break;
           }
       }
       
       //getting all of the commads in one sql line
       String list = " "+attrib.get(0); 
       
       for(int x=1;x<attrib.size();x++)
       {
           list += ", "+attrib.get(x);
       }
       
       
       //excuting the line of sql
       try{
       String sql;
            sql = "SELECT" +list+ " FROM PUBLISHER";
            
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println(listedattrib);
  
            while (rs.next()) {
                //Retrieve by column name
                
                  for(int i=0;i<attrib.size();i++)
                  {
                      String attrabute = rs.getString((String) attrib.get(i));
                      //Display values
                         System.out.print(dispNull(attrabute)+"  ,");  
                  }
                  System.out.println();  
            }
       
            rs.close();
       }catch(Exception e)
       {
           System.out.println("Error:"+e.getMessage());
       }
    } // END showSpecificGroupData
    
    
    
    
        /*
     * showspecific  method is for displaying all the attributes the user chooses from the albums table 
    */
    public static void showSpecificGroupData(Statement stmt) throws SQLException {
       
       System.out.println("Choose which attrabute to insurt then press p to print.");
       ArrayList attrib = new ArrayList<>(); // an array to register every attabute
       boolean run = true;// while finite varable 
       boolean isinlist;//to check if it is already l
       String listedattrib=" ";// collecting every attrabute
       while(run == true)// start of the finite loo[
       {
           System.out.println("Press p to print\n\n1.Group Name\n2.Head Writer\n3.Year Formed\n4.Subject");
           String chose = in.next();// the user choses what to add 
           switch(chose)// to add the specific attribute
           {
               //to add an attribute
               case "1":
                   //getting the title
                   String groupname = "GROUPNAME";
                   //checking if it's already listed
                   isinlist = checkiflisted(attrib, groupname);
                   //if it's not listed then add it to the arraylist
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted group's name\n");
                        attrib.add(groupname);//adding it to the array list
                        listedattrib += "Group Names: ";// adding it to the title attribute
                   } else {
                       System.out.println("\nalready in the list\n");//not listed
                   }
                 
                   break;
               
               case "2":
                   String headwriter = "headwriter";
                   isinlist = checkiflisted(attrib, headwriter);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted head writer\n");
                        attrib.add(headwriter);
                        listedattrib += " Head Writer: ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;
                   
               case "3":
                    String yearformed = "yearformed";
                   isinlist = checkiflisted(attrib, yearformed);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted year formed\n");
                        attrib.add(yearformed);
                        listedattrib += " Year Formed:  ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;
                   
               case "4":
                   String subject = "subject";
                   isinlist = checkiflisted(attrib, subject);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted subject\n");
                        attrib.add(subject);
                        listedattrib += " Subject:  ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;          
               case "p":
                   //printing the listed attributes
                   System.out.println("\nPrinting chosen attrabutes\n");
                   run = false;
                   
                   break;
               case "P":
                   //printing with a capitial letter
                   System.out.println("\nPrinting chosen attrabutes\n");
                   run = false;
                   
                   break;              
               default:
                   //not a listed command
                   System.out.println("\nNot a listed attrabute\n");
                   break;
           }
       }
       
       //getting all of the commads in one sql line
       String list = " "+attrib.get(0); 
       
       for(int x=1;x<attrib.size();x++)
       {
           list += ", "+attrib.get(x);
       }
       
       
       //excuting the line of sql
       try{
       String sql;
            sql = "SELECT" +list+ " FROM WRITINGGROUP";
            
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println(listedattrib);
  
            while (rs.next()) {
                //Retrieve by column name
                
                  for(int i=0;i<attrib.size();i++)
                  {
                      String attrabute = rs.getString((String) attrib.get(i));
                      //Display values
                         System.out.print(dispNull(attrabute)+"  ,");  
                  }
                  System.out.println();  
            }
       
            rs.close();
       }catch(Exception e)
       {
           System.out.println("Error:"+e.getMessage());
       }
    } // END showSpecificGroupData
    
        /*
     * showspecific  method is for displaying all the attributes the user chooses from the albums table 
    */
    public static void showSpecificBookData(Statement stmt) throws SQLException {
       
       System.out.println("Choose which attrabute to insert then press p to print.");
       ArrayList attrib = new ArrayList<>(); // an array to register every attabute
       boolean run = true;// while finite varable 
       boolean isinlist;//to check if it is already l
       String listedattrib=" ";// collecting every attrabute
       while(run == true)// start of the finite loo[
       {
           System.out.println("Press p to print\n\n1.Book Title\n2.Year Published\n3.Number of Pages\n4.Group Name\n5.Publisher Name");
           String chose = in.next();// the user choses what to add 
           switch(chose)// to add the specific attribute
           {
               //to add an attribute
               case "1":
                   //getting the title
                   String Booktitle = "Booktitle";
                   //checking if it's already listed
                   isinlist = checkiflisted(attrib, Booktitle);
                   //if it's not listed then add it to the arraylist
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted Book Title\n");
                        attrib.add(Booktitle);//adding it to the array list
                        listedattrib += "Book Title: ";// adding it to the title attribute
                   } else {
                       System.out.println("\nalready in the list\n");//not listed
                   }
                 
                   break;
               
               case "2":
                   String yearpublished = "yearpublished";
                   isinlist = checkiflisted(attrib, yearpublished);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted year published\n");
                        attrib.add(yearpublished);
                        listedattrib += "Year Published: ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;
                   
               case "3":
                    String numberpages = "numberpages";
                   isinlist = checkiflisted(attrib, numberpages);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted number of pages\n");
                        attrib.add(numberpages);
                        listedattrib += "NumberPages:  ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;
            case "4":
                   String groupname = "groupname";
                   isinlist = checkiflisted(attrib, groupname);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted Group name\n");
                        attrib.add(groupname);
                        listedattrib += "Group Name:  ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break; 
               case "5":
                   String publishername = "publishername";
                   isinlist = checkiflisted(attrib, publishername);
                   if(isinlist == false)
                   {
                        System.out.println("\nInserted publisher name\n");
                        attrib.add(publishername);
                        listedattrib += "Publisher Name:  ";
                   } else {
                       System.out.println("\nalready in the list\n");
                   }
                   break;          
               case "p":
                   //printing the listed attributes
                   System.out.println("\nPrinting chosen attrabutes\n");
                   run = false;
                   
                   break;
               case "P":
                   //printing with a capitial letter
                   System.out.println("\nPrinting chosen attrabutes\n");
                   run = false;
                   
                   break;              
               default:
                   //not a listed command
                   System.out.println("\nNot a listed attrabute\n");
                   break;
           }
       }
       
       //getting all of the commads in one sql line
       String list = " "+attrib.get(0); 
       
       for(int x=1;x<attrib.size();x++)
       {
           list += ", "+attrib.get(x);
       }
       
       
       //excuting the line of sql
       try{
            String sql;
            sql = "SELECT" +list+ " FROM BOOK";
            
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println(listedattrib);
  
            while (rs.next()) {
                //Retrieve by column name
                
                  for(int i=0;i<attrib.size();i++)
                  {
                      String attrabute = rs.getString((String) attrib.get(i));
                      //Display values
                         System.out.print(dispNull(attrabute)+"  ,");  
                  }
                  System.out.println();  
            }
       
            rs.close();
       }catch(Exception e)
       {
           System.out.println("Error:"+e.getMessage());
       }
    } // END showSpecificBookData
   
     /*
     * checkiflisted is a method to check if the user has chosen a method to display
    */
    
    public static boolean checkiflisted(ArrayList a, String check){
        //binary search 
        for(int x = 0; x<a.size(); x++)
        {
            if(a.get(x) == check)
            {
                return true;
            }
        }
        return false;
    }
    
    
     /*
     * dispNull is to return null if the attrabute is null
    */
    
    public static String dispNull(String input) {
        
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    
     /*
     * insert  method is for inserting a new row into the book table
    */
   
    public static void insertNewBook(Statement stmt) throws SQLException {
        //Insert a new book
//        stmt = conn.createStatement();
        in.nextLine();
        System.out.println("Enter the title of the book:");
        String BookTitle = in.nextLine();
        System.out.println("Enter the Year Published:");
        String YearPublished = in.nextLine();
        System.out.println("Enter the Number of Pages:");
        int NumberPages = in.nextInt();
        
        in.nextLine();
        //getting the group name
        System.out.println("\nchoose one of these registered group names");
        //the user has to choose on the listed group in the databace
        showWritingGroups(stmt);
        System.out.println("\nEnter the Group Name:");
        String GroupName = in.nextLine();
        
        //getting the Publisher name
        System.out.println("\nchoose one of these registered group names");
        //the user has to choose on the listed Publisher in the databace
        showPublisher(stmt);
        System.out.println("\nEnter Publisher Name:");
        String PublisherName = in.nextLine();


        try {
            String sql = "INSERT INTO Book (BookTitle, YearPublished, NumberPages, GroupName,PublisherName)"
                                + "VALUES ('" + BookTitle + "', '" + YearPublished + "'," + NumberPages + ", '" + GroupName +"', '"+ PublisherName +"') ";
            stmt.executeUpdate(sql);
            System.out.println("New book have been inserted");
        }catch(Exception e)//catch any exception if the user does any
            {
                System.out.println("Caught Error:"+e.toString());
            }
    }
   /*
     * remove  method is for deleting a row from the book table
    */
    public static void remove(Statement stmt) throws SQLException
    {
        //menu
        System.out.println("1. Delete by Book title");
        System.out.println("2. Delete by Year Published");
        System.out.println("3. Delete by Number of Pages");
        System.out.println("4. Delete by Group Name");
        System.out.println("5. Delete by Publisher Name");
        
        int chose = in.nextInt();// the user choses what to add 
        String attribute = null;
        String delete = null;
        int pageDelete = 0;
        in.nextLine();
        switch(chose)// to add the specific attribute
        {
            case 1: 
                attribute = "BOOKTITLE";
                //show all the title that can be deleted
                showBookTitle(stmt);
                
                System.out.println("Enter the title of the book you would like to delete");
                delete = in.nextLine();
                break;
            case 2:
                attribute = "YEARPUBLISHED";
                
                System.out.println("Enter the year published of the book you would like to delete");
                delete = in.nextLine();                
                break;                                
            case 3:
                attribute = "NUMBERPAGES";
                System.out.println("Enter the year number of pages of the book you would like to delete");
                pageDelete = in.nextInt();                
                break;
            case 4:
                attribute = "GROUPNAME";
                //getting the group name
                System.out.println("\nchoose one of these registered group names");
                //the user has to choose on the listed group in the databace
                showWritingGroups(stmt);
                System.out.println("Enter the year name of the group of the book you would like to delete");
                delete = in.nextLine();                 
                break;
            case 5:
                attribute = "PUBLISHERNAME";
                //getting the Publisher name
                System.out.println("\nchoose one of these registered group names");
                //the user has to choose on the listed Publisher in the databace
                showPublisher(stmt);                
                System.out.println("Enter the year publisher's name of the book you would like to delete");
                delete = in.nextLine();                
                break;   
            default: 
                System.out.println("Invalid chose.");
                break;
        }
        
        
        
        if (attribute != null) {

            try{
                if (delete != null) {
                    //excuting the sql command
                    String sql = "Delete from Book where "+attribute+"='"+delete+"'";
                    stmt.executeUpdate(sql);
                    System.out.print("Book have been deleted"); 
                } else {
                    //excuting the sql command
                    String sql = "Delete from Book where "+attribute+"="+pageDelete;
                    stmt.executeUpdate(sql);
                    System.out.print("Book have been deleted"); 
                }
               

            }catch(SQLException s)//catch any exception if the user does any
            {
                System.out.println("Not delete");
                s.printStackTrace();
            }
        }

    }


     /*
     * insertUpdate method is for inserting and updating all of the method of the same studio
    */
    
    public static void insertUpdate(Statement stmt)throws SQLException 
    {
        System.out.println("Insert a new publisher ...");
       in.nextLine();
        try{
            //getting the publisher name
            System.out.println("Enter the publisher's name");
            String pname = in.nextLine();
            
            //getting the address
            System.out.println("Enter the publisher's address: (123 street) ");
            String paddress = in.nextLine();
            
            //address number
            System.out.print("Enter the publisher phone: (123-123-1234) ");
            String pphone = in.nextLine();
            
            //address email
            System.out.println("Enter the publisher email: (123@123.com) ");
            String pemail = in.nextLine();

            try {
                //excuting sql command
                 String sql;
                 sql = "Insert into PUBLISHER Values( '"+pname+"' , '"+paddress+"' , '"+pphone+"' , '"+pemail+"' )";
            
                 stmt.executeUpdate(sql);
                 System.out.println("have been inserted");
                 //prmopting the user if he/she want to change one to be represented by this studio
                 System.out.println("Albums published by one studio to be published by the new studio:\n1)yes, please.\n2)NO!!");
                 int chose= in.nextInt();
                 
                //if user agrees
                 if(chose == 1)
                 {
                     in.nextLine();
                    //showing all currently userd studios 
                    showPublisher(stmt);
                    //enter the user of which should be updated
                    System.out.println("\nEnter the name of the publisher that will be updated: ");
                    String updatePublisher = in.nextLine();
                    //excuting sql
                    String sqlup = "update BOOK set publishername= '"+pname+"' where publishername= '"+updatePublisher+"'";

                    stmt.execute(sqlup);
                    System.out.println("just updated!");
                 }
            
            }catch(SQLException e)//catching any sql exceptions
            {
                System.out.println("Syntac Error");
            }
            
        }catch(InputMismatchException e)//catching any mismatch exception s
        {
            System.out.println("Error Input Mismatch");
            insertUpdate(stmt);
        } catch(Exception e)//catching any exception not listed
        {
            System.out.println("Caught Exception: "+e.getMessage());
            System.out.println("Need to redo!");
            insertUpdate(stmt); //repeating the prosedure
        }    
    }   
}
