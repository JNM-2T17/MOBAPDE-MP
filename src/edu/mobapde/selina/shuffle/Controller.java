package edu.mobapde.selina.shuffle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class Controller
 */
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println(request.getServletPath());
		switch(request.getServletPath()) {
			case "/Scores":
				Score[] s = ScoreManager.getScores(request.getParameter("gameMode"));
				String reply = (new Gson()).toJson(s);
				response.getWriter().print(reply);
				break;
			case "/Put":
				Score[] scores = (new Gson()).fromJson(request.getParameter("scores"), Score[].class);
				reply = ScoreManager.addScores(scores) + "";
				System.out.println(reply);
				response.getWriter().print(reply);
				break;
			default:
		}
	}

}
