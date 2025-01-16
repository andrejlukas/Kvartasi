import React, { useEffect, useState } from "react";
import { Navbar } from '../../components/ModeratorNavbar'
import "../../styles/Home.css";
import "bootstrap/dist/css/bootstrap.min.css";

export function ModeratorHome(){

    const [productsType, setProductsType] = useState("approved");
    const [error, setError] = useState("");
    const [approvedProducts, setApprovedProducts] = useState([]);
    const [notApprovedProducts, setNotApprovedProducts] = useState([]);

    const [products, setProducts] = useState([]);
  

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }

        fetch(`/api/proizvods/approved`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setApprovedProducts(data);
                setProducts(data);
            })
            .catch((error) => {
                setError(error.message);
            });

        fetch(`/api/proizvods/notApproved`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setNotApprovedProducts(data);
                console.log("aloo");
            })
            .catch((error) => {
                setError(error.message);
            });

    }, []);

    const handleProductsTypeChange = (e) => {
        setProductsType(e.target.value);
        if(e.target.value === "approved") setProducts(approvedProducts);
        else setProducts(notApprovedProducts);
    };

    const OdobriProizvod = (proizvodId) => {
        console.log("odobri "+proizvodId)
    }
    const OdbijProizvod = (proizvodId) => {
        console.log("nije odobren "+proizvodId)
    }
    return (
        <div>
            <div id="vani2" style={{"minHeight": "100vh"}}>
                <Navbar />
                {/* {error && <p>{error}</p>} */}
                <div id="products-alt" className="product-section-alt">
                    <div id="productController">
                        <label>
                            <input
                            type="radio"
                            name="productsType"
                            value="approved"
                            checked={productsType === "approved"}
                            onChange={handleProductsTypeChange}
                            />
                            Odobreni proizvodi
                        </label>
                        <label>
                            <input
                            type="radio"
                            name="productsType"
                            value="notApproved"
                            checked={productsType === "notApproved"}
                            onChange={handleProductsTypeChange}
                            />
                            Proizvodi koji čekaju odobrenje
                        </label>
                    </div>
                    <div className="row">
                        {products.length > 0 ? (
                            products.map((product) => (
                                <div id="productsbox" key={product.proizvodId} className="col-lg-4 col-md-6 col-12 mb-3-alt">
                                    <div className="card-alt product-card-alt">
                                        <img 
                                        src={product.proizvodSlika} 
                                        className="card-img-top-alt" 
                                        alt={product.proizvod_naziv} 
                                        />
                                        <div className="card-body-alt">
                                            <h5 className="card-title-alt">{product.proizvodNaziv}</h5>
                                            <p className="card-text-alt">{product.proizvodOpis}</p>
                                            <div id="gumbcijena-alt">
                                                <p className="price-alt">€{product.proizvodCijena}</p>
                                                {productsType === "notApproved" && (
                                                    <>
                                                        <button className="add-to-cart-btn-alt" onClick={() => OdobriProizvod(product.proizvodId)}>Odobri proizvod</button>
                                                        <button className="add-to-cart-btn-alt" onClick={() => OdbijProizvod(product.proizvodId)}>Odbij proizvod</button>
                                                    </>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <p>Nema proizvoda u ovoj kategoriji.</p>
                        )}
                    </div>
                </div>
            </div>
            
        </div>
    );
}