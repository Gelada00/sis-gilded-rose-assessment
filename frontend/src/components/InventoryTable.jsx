function InventoryTable({ items }) {
    return (
        <table>
            <thead>
                <tr>
                    <th>Item Name</th>
                    <th>SellIn</th>
                    <th>Quality</th>
                </tr>
            </thead>

            <tbody>
                {items.map((item, index) => (
                    <tr key={index}>
                        <td>{item.name}</td>
                        <td>{item.sellIn}</td>
                        <td>{item.quality}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

export default InventoryTable;