import { Navbar } from "../components/Navbar";
import { Link } from "react-router-dom";


import '../styles/KorisnickiRacun.css'


export function KorisnickiRacun(){
    return(
        <div>
            <Navbar/>
            <div className="main-container">
                <div className="mojracun-container">
                    <h1 className="mojracun-naslov">Moj račun:</h1>
                    <ul>
                        <li>
                            <Link to="/mojipodaci" className="link">Moji podaci</Link>
                        </li>
                        <li>
                            <Link to="/mojiracuni" className="link">Moji računi</Link> {/* koristila sam link */}
                        </li>
                        <li>
                            <Link to="/mojerecenzije" className="link">Moje recenzije</Link>
                        </li>
                    </ul>

                </div>
            </div>
        </div>
    )
    
}