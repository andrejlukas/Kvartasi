import React, { useEffect, useState } from "react";
import { Navbar } from "../../components/AdminNavbar";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/ModeratorRecenzije.css";

export function AdminRecenzije() {
  const [reviewsType, setReviewsType] = useState("approved");
  const [error, setError] = useState("");
  const [approvedReviews, setApprovedReviews] = useState([]);
  const [notApprovedReviews, setNotApprovedReviews] = useState([]);
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };

    fetch(`/api/recenzijas/approved`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        setApprovedReviews(data);
        if (reviewsType === "approved") {
          setReviews(data);
        }
      })
      .catch((error) => {
        setError(error.message);
      });

    fetch(`/api/recenzijas/not-approved`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        setNotApprovedReviews(data);
        if (reviewsType === "notApproved") {
          setReviews(data);
        }
      })
      .catch((error) => {
        setError(error.message);
      });
  }, [reviewsType]);

  const handleReviewsTypeChange = (e) => {
    setReviewsType(e.target.value);
    if (e.target.value === "approved") {
      setReviews(approvedReviews);
    } else {
      setReviews(notApprovedReviews);
    }
  };

  function updateReview(reviewData) {
    const token = localStorage.getItem("token");
    const options = {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(reviewData),
    };

    fetch(`/api/recenzijas/odobrioModerator/${reviewData.recenzijaId}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
      })
      .then(() => {
        setNotApprovedReviews((prevReviews) =>
          prevReviews.filter((review) => review.recenzijaId !== reviewData.recenzijaId)
        );
        if (reviewData.odobrioModerator) {
          setApprovedReviews((prevReviews) => [...prevReviews, reviewData]);
        }
        if (reviewsType === "approved") {
          setReviews((prevReviews) => [...prevReviews, reviewData]);
        } else {
          setReviews((prevReviews) =>
            prevReviews.filter((review) => review.recenzijaId !== reviewData.recenzijaId)
          );
        }
      })
      .catch((error) => {
        setError(error.message);
      });
  }

  const OdobriRecenziju = (review) => {
    review.odobrioModerator = true;
    updateReview(review);
  };

  async function ObrisiRecenziju(reviewData){
    const token = localStorage.getItem("token");
    if (!token) {
        setError("Nedostaje token za autorizaciju.");
        return;
    }

    const options = {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
    };

    try {
        // Brisanje recenzije
        const deleteResponse = await fetch(`/api/recenzijas/${reviewData.recenzijaId}`, options);
        if (!deleteResponse.ok) {
            throw new Error(`Brisanje recenzije nije uspjelo. Status: ${deleteResponse.status}`);
        }

        // Dohvaćanje ažuriranih recenzija
        const recenzijeOptions = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        };
        fetch(`/api/recenzijas/approved`, recenzijeOptions)
        .then(async (response) => {
          if (!response.ok) {
            const text = await response.text();
            throw new Error(text);
          }
          return response.json();
        })
        .then((data) => {
          setApprovedReviews(data);
          if (reviewsType === "approved") {
            setReviews(data);
          }
        })
        .catch((error) => {
          setError(error.message);
        });
  
      fetch(`/api/recenzijas/not-approved`, recenzijeOptions)
        .then(async (response) => {
          if (!response.ok) {
            const text = await response.text();
            throw new Error(text);
          }
          return response.json();
        })
        .then((data) => {
          setNotApprovedReviews(data);
          if (reviewsType === "notApproved") {
            setReviews(data);
          }
        })
        .catch((error) => {
          setError(error.message);
        });


    } catch (error) {
        console.error("Pogreška prilikom obrade recenzija:", error);
        setError(error.message);
    }

  }

 /*  const OdbijRecenziju = (review) => {
    review.odobrioModerator = false;
    updateReview(review);
  }; */

  return (
    <div>
      <div id="vani2" style={{ minHeight: "100vh" }}>
        <Navbar />
        <div id="reviews-alt1" className="review-section-alt1">
          <div id="reviewController1">
            <label>
              <input
                type="radio"
                name="reviewsType"
                value="approved"
                checked={reviewsType === "approved"}
                onChange={handleReviewsTypeChange}
              />
              Odobrene recenzije
            </label>
            <label>
              <input
                type="radio"
                name="reviewsType"
                value="notApproved"
                checked={reviewsType === "notApproved"}
                onChange={handleReviewsTypeChange}
              />
              Recenzije koje čekaju odobrenje
            </label>
          </div>
          <div className="row1">
            {reviews.length > 0 ? (
              reviews.map((review) => (
                <div
                  id="reviewsbox1"
                  key={review.recenzijaId}
                  className="col-lg-4 col-md-6 col-12 mb-3-alt"
                >
                  <div className="card-alt review-card-alt1">
                    <div className="card-body-alt1">
                      <h5 className="card-title-alt1">Recenzija ID: {review.recenzijaId}</h5>
                      <p className="card-text-alt1" id="opisrec">{review.recenzijaOpis}</p>
                      <p className="card-text-alt1"><strong>Odgovor: </strong>{review.recenzijaOdgovor == null ? "Nema odgovora" : review.recenzijaOdgovor}</p>
                      <p className="card-text-alt1"><strong>Ocjena: </strong>{review.recenzijaZvjezdice}</p>
                      <p className="card-text-alt1"><strong>Kupac ID: </strong> {review.kupacId}</p>
                      <p className="card-text-alt1"><strong>Trgovina ID: </strong> {review.trgovinaId}</p>
                      {reviewsType === "notApproved" && (
                        <>
                          <button
                            className="add-to-cart-btn-yes"
                            onClick={() => OdobriRecenziju(review)}
                          >
                            Odobri recenziju
                          </button>
                         {/*  <button
                            className="add-to-cart-btn-no"
                            onClick={() => OdbijRecenziju(review)}
                          >
                            Odbij recenziju
                          </button> */}
                          <button
                            className="add-to-cart-btn-no"
                            onClick={() => ObrisiRecenziju(review)}
                          >
                            Obriši recenziju
                          </button>
                        </>
                      )}
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <p>Nema recenzija u ovoj kategoriji.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
