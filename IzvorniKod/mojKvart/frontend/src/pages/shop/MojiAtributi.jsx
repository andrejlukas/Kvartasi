import { useState, useEffect } from "react";
import { Navbar } from "../../components/ShopNavbar";
import "../../styles/Attributes.css";


export function ShopMojiAtributi() {
    const [shopEmail, setShopEmail] = useState(null);
    const [shopId, setShopId] = useState(null);
    const [error, setError] = useState("");
    const [newAttribute, setNewAttribute] = useState("");
    const [attributes, setAttributes] = useState([]);
    const [shopAttributes, setShopAttributes] = useState([]);

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
                setShopId(data.trgovinaId);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, [shopEmail]);

    /*useEffect(() => {
        if(!shopId) return;
        if(!attributes) return;

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
                setShopId(data.trgovinaId);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, [shopEmail]);*/
    

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
            .catch((error) => { setPopupError(error.message); });
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
      </div>
    );
}