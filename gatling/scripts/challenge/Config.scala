package challenge 
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

/**
 * Initialize the Challenge
 */
object Config {

	val httpConf = httpConfig.baseURL("http://localhost:9000/")
  val stadiums = 5;
  val pricelists = 3;
  val blocks = 100; 
  val rowSeats = List((100,200,"p-1"),(10,1000,"p-2"),(1000,10,"p-3")) 
}