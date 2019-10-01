/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package free_rice;
import java.io.PrintWriter;
import java.lang.*;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RiceFarm {
    WebDriver driver;
    Question[] solved;
    
    public RiceFarm(String username, String password){
        driver = new FirefoxDriver();
        driver.navigate().to("http://freerice.com/user/login");
        WebElement name = driver.findElement(By.id("edit-name_watermark"));
        name.sendKeys(username);
        WebElement pass = driver.findElement(By.id("edit-pass"));
        pass.sendKeys(password);
        WebElement enter = driver.findElement(By.id("edit-submit"));
        enter.click();
    }
    
    
    
    public RiceFarm(String username, String password, String group){
        driver = new FirefoxDriver();
        driver.navigate().to("http://freerice.com/user/login");
        WebElement name = driver.findElement(By.id("edit-name_watermark"));
        name.sendKeys(username);
        WebElement pass = driver.findElement(By.id("edit-pass"));
        pass.sendKeys(password);
        WebElement enter = driver.findElement(By.id("edit-submit"));
        enter.click();
        driver.navigate().to("http://freerice.com/users/SteveJobs");
        WebElement play = driver.findElement(By.partialLinkText("play"));
        play.click();
        delay(3000);
        WebElement vocab = driver.findElement(By.linkText("English Vocabulary"));
        vocab.click();
        solved = new Question[0];
    }
    
    public void saveMemory(){
        try{
        PrintWriter writer = new PrintWriter("C:\\Users\\Steve\\Desktop\\in.txt", "UTF-8");
        for(int i = 0; i < solved.length; i++){
            writer.println(solved[i].question + " = " + solved[i].answer);
        }
        writer.close();
        }catch(Exception e){
            System.out.println("ERROR SAVING");
        }
    }
    
    public void vocabFarm(int grains){
        int total = 0;
        while(total<grains){
            if(answerVocab()){
                total = total + 10;
                if(total%100==0){
                    System.out.println("Saving memory");
                    saveMemory();
                }
            }
        }
        System.out.println(grains+" grains farmed \n SUMMARY:");
        printSolved();
        driver.close();
    }
    
    public boolean learnQuestion(String question){
        boolean lucky = false;
        
        try{
            WebElement first = driver.findElement(By.className("answer-item"));
            first.click();
            delay(2000);
            WebElement response = driver.findElement(By.className("block-top"));
            String trueAnswer = response.getText();
            if(trueAnswer.indexOf("Correct!")!=-1){
                lucky = true;
                System.out.println("LUCKY GUESS");
            }
            if(trueAnswer.indexOf(question) == -1){
                System.out.println("SOMETHING WONG BPOI");
                return false;
            }
            trueAnswer = trueAnswer.substring(trueAnswer.indexOf('=')+2,trueAnswer.length());
            System.out.println("Answer Learned: "+trueAnswer);
            solved = expand(solved, new Question(question, trueAnswer));
            return lucky;
        }catch(Exception e){
            return false;
        }
    }
    
    public void printSolved(){
        for(int i = 0; i < solved.length; i++){
            System.out.println(solved[i].question+" = "+solved[i].answer);
        }
    }
    
    public boolean answerVocab(){
        delay(1000);
        try{
            WebElement question = driver.findElement(By.className("question-link"));
            String quest = question.getText();
            quest = quest.substring(0,quest.indexOf(' '));
            System.out.println("Word: "+quest);
            String out = findQuestion(quest);
            if(out == null){
                return learnQuestion(quest);
            }
            
            
            WebElement answer = driver.findElement(By.partialLinkText(out));
            answer.click();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
            return false;
        }
    }
    
    public String findQuestion(String in){
        for(int i = 0; i < solved.length; i++){
            if(in.equals(solved[i].question)){
                System.out.println("Know that one, returning "+solved[i].answer);
                return solved[i].answer;
            }
        }
        
        return null;
    }
    
    public boolean answerMath(){
        delay(1000);
        
        try{
            WebElement question = driver.findElement(By.className("question-link"));
            String quest = question.getText();
            int a = Integer.parseInt(quest.substring(0,quest.indexOf('x')-1));
            int b = Integer.parseInt(quest.substring(quest.indexOf('x')+2,quest.length()));
            int product = a*b;
            WebElement answer = driver.findElement(By.linkText(Integer.toString(product)));
            answer.click();
            return true;
        }catch(Exception e){
            System.out.println("Error");
            return false;
        }
    }
    
    
    public void mathFarm(int grains){
        driver.navigate().to("http://freerice.com/#/multiplication-table/17507");
        int rice = 0;
        int questions = (grains)/10;
        
        for(int i = 0; i < questions; i++){
        while(true){
            if(answerMath()==true){
                break;
            }
        }
        }
        

    }
    
    public void farm(int grains){
        int rice = 0;
        int questions = (2*grains)/10;
        for(int i = 0; i < questions; i++){
            driver.navigate().to("http://freerice.com/#/english-vocabulary/1542");
            delay(250);
            try{
            WebElement answer = driver.findElement(By.linkText("yell"));
            answer.click();
            }catch(Exception e){
                rice = rice - 10;
            }
            rice = rice + 10;
            System.out.println("Round: "+(i+1)+" Rice earned: "+rice);
        }
    }
    
    
    public static void delay(int delay){
        try{
            Thread.sleep(delay);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    public Question[] expand(Question[] OrigArray, Question toAdd) {
        Question[] newArray = new Question[OrigArray.length + 1];
        System.arraycopy(OrigArray, 0, newArray, 0, OrigArray.length);
        newArray[newArray.length-1] = toAdd;
        return newArray;
    }
}
