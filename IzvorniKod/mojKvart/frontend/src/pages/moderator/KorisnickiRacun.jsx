import { Navbar } from "../../components/ModeratorNavbar";
import '../../styles/KorisnickiRacun.css'


export function ModeratorKorisnickiRacun(){
    return(
        <div className="korisnicki-racun-wrappper">
            <Navbar/>
            <div className="main-container">
                <div className="mojracun-container">
                    <h1 className="mojracun-naslov">Moj račun:</h1>
                    <ul>
                        <li>
                            <a href="/podacimoderator" className="korisnicki-racun-link">Moji podaci</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    )
    
}