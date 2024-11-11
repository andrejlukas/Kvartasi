import { Link } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import "../styles/home.css"
import { Navbar } from "../components/Navbar";

export function Home(){
    return (
        
        <div>
            <Navbar/>
            <div className="main-container"> {/* koristim main-container za sve osim header-a */}
                
                <h1 style={{textAlign:"center"}}>
                    Home - Uspješna prijava!
                </h1>
            </div>
            
        </div>
    );
}