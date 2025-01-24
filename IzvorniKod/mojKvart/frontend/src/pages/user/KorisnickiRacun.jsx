import { Navbar } from "../../components/Navbar";
import '../../styles/KorisnickiRacun.css'


export function KorisnickiRacun(){
    return(
        <div className="korisnicki-racun-wrappper">
            <Navbar/>
            <div className="main-container">
                <div className="mojracun-container">
                    <h1 className="mojracun-naslov">Moj račun:</h1>
                    <ul>
                        <li>
                            <a href="/mojipodaci" className="korisnicki-racun-link">Moji podaci</a>
                        </li>
                        <li>
                            <a href="/mojiracuni" className="korisnicki-racun-link">Moji računi</a>
                        </li>
                        <li>
                            <a href="/mojerecenzije" className="korisnicki-racun-link">Moje recenzije</a>
                        </li>
                    </ul>

                </div>
            </div>
        </div>
    )
    
}