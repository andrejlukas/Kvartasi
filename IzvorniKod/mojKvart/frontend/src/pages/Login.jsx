import { Link } from "react-router-dom";
import IMAGE from "../assets/vege.avif" //treba nam bolja slika 
import "../styles/login.css"

export function Login() {

return (
   <div className="login-container">
      <div className="image-container">
         <div className="overlay">            
            <div className="overlay-box">
               
               <h1>
               <span style={{ fontWeight: "bold", color: "black" }}>Moj</span>
               <span style={{ fontWeight: "bold", color: "#098443" }}>Kvart</span>
               </h1>
               
               <p>Kupuj lokalno!</p>
            </div>
         </div>        
        <img src={IMAGE} className="responsive-image" />
      
      </div>
      <div className="big-container ">
              
         <form className="login-form">
            <h2 className="naslov">Log in</h2>

            <label htmlFor="email">Email address</label>
            <input type="email" id="email" name="email"  className="inputs"/>

            <div className="password-container">
               <label htmlFor="password">Your password</label>
            {/* <span className="show-hide">Hide</span> */}
            </div>
            <input type="password" id="password" name="password" className="inputs" />

            <button type="submit">Log in</button>

            <div className="divider"></div>

            <p className="signup-text">Don't have an account?</p>
            <Link to="/signup">
               <button type="button">Sign up</button>
            </Link>
         </form>
      </div>
    </div>
);
}