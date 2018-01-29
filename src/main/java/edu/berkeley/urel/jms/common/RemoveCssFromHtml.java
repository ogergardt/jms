package edu.berkeley.urel.jms.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class RemoveCssFromHtml {

	public static String removeCss(String input){
		input = input.replaceAll("(?s)<!--.*?-->", ""); 
		if(input.contains("<") && input.contains(">")){
			if(input.contains("<h1")){
				input = input.replaceAll("<h1", "<h4");
				
			}
			if(input.contains("h1>")){
				input = input.replaceAll("h1>", "h4>");
				
			}
			if(input.contains("<h2")){
				input = input.replace("<h2", "<h4");
				
			}
			if(input.contains("h2>")){
				input = input.replace("h2>", "h4>");
				
			}
			if(input.contains("<h3")){
				input = input.replace("<h3", "<h4");
				
			}
			if(input.contains("h3>")){
				input = input.replace("h3>", "h4>");
				
			}
			if(input.contains("<h4")){
				input = input.replace("<h4", "<b");
				
			}
			if(input.contains("h4>")){
				input = input.replace("h4>", "b>");
				
			}
			if(input.contains("<textarea")){
				input = input.replace("<textarea", "<p");
			}
			if(input.contains("textarea>")){
				input = input.replace("textarea>", "p>");
			}
			if(input.contains("<blockquote")){
				input = input.replace("<blockquote", "<p");
			}
			if(input.contains("blockquote>")){
				input = input.replace("blockquote>", "p>");
			}
			if(input.contains("<pre")){
				input = input.replace("<pre", "<p");
			}
			if(input.contains("pre>")){
				input = input.replace("pre>", "p>");
			}
			if(input.contains("<a")){
				input = input.replace("<a", "<span");
			}
			if(input.contains("a>")){
				input = input.replace("a>", "span>");
			}
			if(input.contains("<small")){
				input = input.replace("<small", "<p");
			}
			if(input.contains("small>")){
				input = input.replace("small>", "p>");
			}
		String modifiedHtml = "";
		Document doc = Jsoup.parse(input);
		doc.select("img").remove();
		doc.select("input").remove();
		doc.select("button").remove();
		doc.select("iframe").remove();
		doc.select("script").remove();
		doc.select("style").remove();
		doc.select("link").remove();
		//doc.select("a").remove();
		NodeTraversor traversor  = new NodeTraversor(new NodeVisitor() {

		  public void tail(Node node, int depth) {
		    if (node instanceof Element) {
		        Element e = (Element) node;

		        e.removeAttr("class");
		        e.removeAttr("id");
		        e.removeAttr("style");
		        e.removeAttr("size");
		        e.removeAttr("href");
		        e.removeAttr("onclick");
		        e.removeAttr("src");
		        e.removeAttr("bgcolor");
		        e.removeAttr("color");
//		        e.removeAttr("border");
		        e.removeAttr("align");
		        e.removeAttr("face");
		        e.removeAttr("sans-serif");
		    }
		  }

		  public void head(Node node, int depth) {        
		  }
		});
		
		traversor.traverse(doc.body());
		modifiedHtml = doc.toString();
		modifiedHtml = modifiedHtml.replace("<html>", "")
				.replace("</html>", "").replace("<body>", "")
				.replace("<head>", "").replace("</head>", "")
				.replace("</body>", "").trim();
		return modifiedHtml;
		}else{
		return input;
		}
		
	}
	
	public static void main(String[] args) {
	
		String html = "<a href=\"mailto:\"><img src=\"images/MailBut.gif\" alt=\"email to friend\" /></a><br /> <br /> <br /> <h1> haii </h1><blockquote>hellooo </blockquote>";
		// <img alt=\"\" src=\"/Include/Images/Design/common/pictos/green-arrow.png\" style=\"vertical-align:middle;\" /><h1>hello</h1>
		System.out.println(removeCss(html));
	}
}

