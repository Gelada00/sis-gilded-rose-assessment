import { useState } from "react";

function AddItemForm({ onAddItem }) {
    const [itemType, setItemType] = useState("Normal Item");
    const [sellIn, setSellIn] = useState("5");
    const [quality, setQuality] = useState("10");
    const [error, setError] = useState("");

    const isSulfuras = itemType === "Sulfuras";

    const addItem = (event) => {
        event.preventDefault();

        setError("");

        if (sellIn.trim() === "" || (!isSulfuras && quality.trim() === "")) {
            setError("Please enter valid values for SellIn and Quality.");
            return;
        }

        const newSellIn = Number(sellIn);
        const newQuality = isSulfuras ? 80 : Number(quality);

        if (!Number.isInteger(newSellIn) || !Number.isInteger(newQuality)) {
            setError("SellIn and Quality must be whole numbers.");
            return;
        }

        if (newQuality < 0) {
            setError("Quality cannot be negative.");
            return;
        }

        const newItem = {
            name: itemType,
            sellIn: newSellIn,
            quality: newQuality,
        };
        onAddItem(newItem);
    }

    return (

        <form onSubmit={addItem} noValidate>
            <h2> Add Item</h2>
            <div>

                <label htmlFor="itemType">Item Type:</label>

                <select
                    id="itemType"
                    value={itemType}
                    onChange={(event) => setItemType(event.target.value)}
                >
                    <option value="Normal Item">Normal Item</option>
                    <option value="Aged Brie">Aged Brie</option>
                    <option value="Sulfuras">Sulfuras</option>
                    <option value="Conjured">Conjured</option>
                    <option value="Backstage passes">Backstage passes</option>
                </select>
            </div>

            <div>
                <label htmlFor="sellIn">SellIn:</label>
                <input
                    type="number"
                    id="sellIn"
                    value={sellIn}
                    onChange={(event) => {
                        setSellIn(event.target.value);
                        setError("");
                    }}
                />
            </div>

            <div>
                <label htmlFor="quality">Quality:</label>
                <input
                    type="number"
                    id="quality"
                    value={isSulfuras ? 80 : quality}
                    onChange={(event) => {
                        setQuality(event.target.value);
                        setError("");
                    }}
                    disabled={isSulfuras}
                />
            </div>

            {error && (
                <p role="alert" className="form-error">
                    {error}
                </p>
            )}

            <button type="submit">
                Add Item
            </button>
        </form >
    );
}

export default AddItemForm;