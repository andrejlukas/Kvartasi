import { Navbar } from "../../components/ShopNavbar";
import '../../styles/KorisnickiRacun.css'

export function ShopKorisnickiRacun(){

    return(
        <div>
            <div className="korisnicki-racun-wrappper" id="toBlur">
                <Navbar/>
                <div className="main-container">
                    <div className="mojracun-container">
                        <h1 className="mojracun-naslov">Moj račun:</h1>
                        <ul>
                            <li>
                                <a href="/podacitrgovine" className="korisnicki-racun-link">Podaci trgovine</a>
                            </li>
                            <li>
                                <a href="/atributitrgovine" className="korisnicki-racun-link">Dodatne značajke trgovine</a>
                            </li>
                            <li>
                                <a href="/recenzijetrgovine" className="korisnicki-racun-link">Recenzije</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    )
}