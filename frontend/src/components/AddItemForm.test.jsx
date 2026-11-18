/**
 * @vitest-environment jsdom
 */

import { test, expect, vi} from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import AddItemForm from './AddItemForm';

test('adds an item with valid values', async () => {
    const onAddItem = vi.fn();
    const user = userEvent.setup();

    render(<AddItemForm onAddItem={onAddItem} />);

    await user.selectOptions(
        screen.getByLabelText('Item Type:'),
        'Aged Brie'
    );

    const sellInInput = screen.getByLabelText('SellIn:');
    await user.clear(sellInInput);
    await user.type(sellInInput, '7');

    const qualityInput = screen.getByLabelText('Quality:');
    await user.clear(qualityInput);
    await user.type(qualityInput, '20');

    await user.click(
        screen.getByRole('button', { name: 'Add Item' })
    );

    expect(onAddItem).toHaveBeenCalledWith({
        name: 'Aged Brie',
        sellIn: 7,
        quality: 20
    });
});