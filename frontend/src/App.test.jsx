/**
 * @vitest-environment jsdom
 */

import { test, expect, vi, afterEach } from 'vitest';
import { render, screen, within, waitFor, cleanup } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from './App';
import { updateInventory } from './api/inventoryApi';

/// Replace the actual API call with a mock for testing
vi.mock('./api/inventoryApi', () => ({
    updateInventory: vi.fn()
}));

afterEach(() => {
    cleanup();
    vi.clearAllMocks();
});

test('updates inventory when "Advance One Day" button is clicked', async () => {
    const user = userEvent.setup();

    const updatedItems = [
        { name: "Normal Item", sellIn: 4, quality: 9 },
        { name: "Aged Brie", sellIn: 2, quality: 21 },
        { name: "Sulfuras", sellIn: 0, quality: 80 },
        { name: "Conjured", sellIn: 1, quality: 8 },
    ];

    updateInventory.mockResolvedValue(updatedItems);

    render(<App />);

    await user.click(screen.getByRole('button', { name: 'Advance One Day' }));

    expect(updateInventory).toHaveBeenCalledOnce();

    await screen.findByText('9');

    const table = screen.getByRole('table');

    const normalItemRow = within(table).getByText('Normal Item').closest('tr');


    expect(within(normalItemRow).getByText('4')).toBeTruthy();
    expect(within(normalItemRow).getByText('9')).toBeTruthy();
});

test('shows an error when the API request fails', async () => {
    const user = userEvent.setup();

    // simulate an API failure
    updateInventory.mockRejectedValue(new Error('API unavailable'));

    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => { });

    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => { });

    try {
        render(<App />);

        await user.click(screen.getByRole('button', { name: 'Advance One Day' }));

        // An error is displayed
        await waitFor(() => {
            expect(alertSpy).toHaveBeenCalledWith(
                'Error updating inventory:API unavailable'
            );
        });

        // The inventory should remain unchanged
        const table = screen.getByRole('table');

        const normalItemRow = within(table).getByText('Normal Item').closest('tr');

        expect(within(normalItemRow).getByText('5')).toBeTruthy();
        expect(within(normalItemRow).getByText('10')).toBeTruthy();

        // The button should be enabled again
        expect(
            screen.getByRole('button', { name: 'Advance One Day' })
                .disabled
        ).toBe(false);
    } finally {
        alertSpy.mockRestore();
        consoleSpy.mockRestore();
    }
});