import { useState, useEffect } from "react";
import { Navbar } from "../../components/ShopNavbar";
import "../../styles/Attributes.css";


export function ShopMojiAtributi() {
    const [shopEmail, setShopEmail] = useState("");
    const [shopData, setShopData] = useState({
        trgovinaId: null,
        trgovinaNaziv: "",
        trgovinaOpis: "",
        trgovinaKategorija: "",
        trgovinaLokacija: "",
        trgovinaSlika: "",
        trgovinaRadnoVrijemeOd: "",
        trgovinaRadnoVrijemeDo: "",
        trgovinaEmail: shopEmail,
        trgovinaSifra: "",
        imaAtributeAtributs: []
    });
    const [error, setError] = useState("");
    const [newAttribute, setNewAttribute] = useState("");
    const [attributes, setAttributes] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        let options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }, body: JSON.stringify({ "oneLiner": token })
        };
        
        fetch('/api/tokens/claims', options)
            .then(async response => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then(data => {
                setShopEmail(data.email);
            })
            .catch(error => setError(error.message));

        options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };

        fetch('/api/atributs', options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setAttributes(data);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, []);

    useEffect(() => {
        if(!shopEmail) return;

        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }
        
        fetch(`/api/trgovinas/${shopEmail}`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setShopData(data);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, [shopEmail]);
    

    const handleAttributeAddition = () => {
        const token = localStorage.getItem("token");
        const options = {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            }, body: JSON.stringify({ "atributOpis": newAttribute })
        }

        fetch("/api/atributs", options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.text();
            })
            .then(resp => { window.location.reload(); })
            .catch((error) => { setError(error.message); });
    };

    const handleCheckboxChange = (attributeId, checked) => {
        setShopData((prev) => {
            const updatedAttributes = checked
                ? [...prev.imaAtributeAtributs, attributeId]
                : prev.imaAtributeAtributs.filter((id) => id !== attributeId);
                console.log(updatedAttributes);
            return {
                ...prev,
                imaAtributeAtributs: updatedAttributes
            };
        });
    };
    
    const saveAttributes = () => {
        const token = localStorage.getItem("token");
        const options = {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }, body: JSON.stringify(shopData)
        };
    
        fetch(`/api/trgovinas/${shopData.trgovinaId}`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.text();
            })
            .then((text) => window.location.reload())
            .catch((error) => setError(error.message));
    };
    
    
   
    return (
      <div>
        <Navbar/>
        <div id="addAttributesController">
            <input type="text" 
                   placeholder="Želite li dodati novu značajku?" 
                   value={newAttribute} 
                   onChange={ e => setNewAttribute(e.target.value)}
            />
            <button onClick={handleAttributeAddition}>Dodaj novu značajku</button>
        </div>
        {error && <p style={{"color": "red"}}>{error}</p>}
        <h4>Označite vaše specijalne značajke:</h4>
        {attributes && shopData &&
            <div>
                {attributes.map((attribute) => (
                    <div key={attribute.atributId}>
                        <label>
                            <input
                                type="checkbox"
                                checked={shopData.imaAtributeAtributs.includes(attribute.atributId)}
                                onChange={(e) =>
                                    handleCheckboxChange(attribute.atributId, e.target.checked)
                                }
                            />
                            {attribute.atributOpis}
                        </label>
                    </div>
                ))}
            </div>
        }
        <button onClick={saveAttributes}>Ažuriraj dodatne značajke</button>
      </div>
    );
}