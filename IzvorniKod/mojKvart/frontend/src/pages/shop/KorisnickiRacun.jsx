import { useState, useEffect } from "react";
import { Navbar } from "../../components/ShopNavbar";
import '../../styles/KorisnickiRacun.css'

export function ShopKorisnickiRacun(){
    const [shopEmail, setShopEmail] = useState(null);
    const [shopId, setShopId] = useState(null);
    const [error, setError] = useState(null);
    const [popupError, setPopupError] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ "oneLiner": token }),
            };
        
        fetch('/api/tokens/claims', options)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {throw new Error(text)});
                }
                return response.json();
            })
            .then(data => {
                setShopEmail(data.email);
            })
            .catch(error => setError(error.message));
        }, []);

    useEffect(() => {
        if (!shopEmail) return;

        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }
        
        fetch(`/api/trgovinas/${shopEmail}`, options)
            .then((response) => {
                if (!response.ok) {
                    return response.text().then(text => {throw new Error(text)});
                }
                return response.json();
            })
            .then((data) => {
                setShopId(data.trgovinaId);
            })
            .catch((error) => {
                setError(error.message);
            });
        }, [shopEmail]);

    function deleteAccountPopUp() {
        const element = document.getElementById("toBlur");
        element.style.cursor = "not-allowed";
        element.style.opacity = 0.5;
        document.getElementById("deletePopup").style.display = "flex";
    }

    function deleteYesOption() {
        const confirmationText = document.getElementById("text").value;

        if (confirmationText !== "obrisi-moj-racun") {
            setPopupError('Potrebno je upisati "obrisi-moj-racun"!');
            return;
        }

        const token = localStorage.getItem('token');
        const options = {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }
        
        fetch(`/api/trgovinas/${shopId}`, options)
            .then((response) => {
                if (!response.ok) {
                    return response.text().then(text => {throw new Error(text)});
                }
                return response.json();
            })
            .catch((error) => {
                setError(error.message);
            });

        window.location.href = "/";
        localStorage.clear();
    }

    function deleteNoOption() {
        const element = document.getElementById("toBlur");
        element.style.cursor = "auto";
        element.style.opacity = 1;
        document.getElementById("deletePopup").style.display = "none";
        setPopupError(null);
        document.getElementById("text").value = "";
    }
    
    return(
        <div>
            <div className="korisnicki-racun-wrappper" id="toBlur">
                <Navbar/>
                {error && <p>{error}</p>}
                <div className="main-container">
                    <div className="mojracun-container">
                        <h1 className="mojracun-naslov">Moj račun:</h1>
                        <ul>
                            <li>
                                <a href="/podacitrgovine" className="korisnicki-racun-link">Moji podaci</a>
                            </li>
                            <li>
                                <a href="/recenzijetrgovine" className="korisnicki-racun-link">Recenzije</a>
                            </li>
                            <li>
                                <a href="#" className="korisnicki-racun-link" onClick={deleteAccountPopUp}>Obriši račun</a>
                            </li>
                        </ul>

                    </div>
                </div>
            </div>

            <div id="deletePopup">
                <p className="popupItem">Jeste li sigurni da želite obrisati vaš račun?</p>
                <p className="popupItem">Ako jeste, upišite "obrisi-moj-racun".</p>
                <input className="popupItem" id="text" type="text" placeholder="Obriši račun?"/>
                {popupError && <p className="popupItem" style={{color: "red"}}>{popupError}</p>}
                <div className="popupItem" id="YesNoButtons">
                    <button onClick={deleteYesOption}>Obriši</button>
                    <button onClick={deleteNoOption}>Odustani</button>
                </div>
            </div>
        </div>
    )
}