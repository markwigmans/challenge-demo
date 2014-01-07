package challenge 

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

/**
 * Configure the Challenge simulation
 */
object Config {

	val httpConf = httpConfig.baseURL("http://localhost:9000/")
  
  val stadiums = 5;
  val pricelists = 3;
  val blocks = 1; 
  //val rowSeats = List((100,200,"p-1"),(10,1000,"p-2"),(1000,10,"p-3")) 
  val rowSeats = List((1,1,"p-1"))
  
  val users = 2;
  val ramp = 1;
  val runs = 10;
  //val ticketRequest = List("""["kids","kids","adults","seniors"]""","""["kids","kids"]""")
  val ticketRequest = List("""["kids"]""")
}