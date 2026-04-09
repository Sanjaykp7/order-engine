document.addEventListener('DOMContentLoaded', () => {
    fetchProducts();
    
    document.getElementById('checkout-btn').addEventListener('click', processOrder);
});

async function fetchProducts() {
    const productsContainer = document.getElementById('products-container');
    const productSelect = document.getElementById('product-select');
    
    try {
        const response = await fetch('/products');
        if (!response.ok) throw new Error('Failed to fetch products');
        
        const products = await response.json();
        
        productsContainer.innerHTML = '';
        productSelect.innerHTML = '';
        
        if (products.length === 0) {
            productsContainer.innerHTML = '<div class="loader">No products found.</div>';
            return;
        }

        products.forEach(product => {
            const stockClass = product.stock < 5 ? 'low-stock' : '';
            const productHtml = `
                <div class="product-item">
                    <div class="product-info">
                        <h3>${product.name}</h3>
                        <div class="product-price">$${product.price.toFixed(2)}</div>
                    </div>
                    <div class="product-stock ${stockClass}">
                        ${product.stock} in stock
                    </div>
                </div>
            `;
            productsContainer.insertAdjacentHTML('beforeend', productHtml);

            const option = document.createElement('option');
            option.value = product.productId;
            option.textContent = `${product.name} - $${product.price.toFixed(2)}`;
            productSelect.appendChild(option);
        });
        
    } catch (error) {
        productsContainer.innerHTML = `<div class="status-message status-error">Error connecting to server.</div>`;
        console.error(error);
    }
}

async function processOrder() {
    const btn = document.getElementById('checkout-btn');
    const statusBox = document.getElementById('status-message');
    
    const productId = document.getElementById('product-select').value;
    const quantity = document.getElementById('quantity-input').value;
    
    const idempotencyKey = 'req-' + Date.now() + '-' + Math.floor(Math.random() * 1000);
    
    const orderPayload = {
        userId: 1, 
        idempotencyKey: idempotencyKey,
        items: [
            {
                productId: parseInt(productId),
                quantity: parseInt(quantity)
            }
        ]
    };

    btn.disabled = true;
    btn.textContent = 'Processing Transaction...';
    statusBox.className = 'status-message hidden';
    
    try {
        const response = await fetch('/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderPayload)
        });
        
        if (response.ok) {
            const data = await response.json();
            showStatus(`Success! Order #${data.id} Complete.`, 'status-success');
        } else {
            const errorText = await response.text();
            showStatus(`Failed: ${errorText}`, 'status-error');
        }
        
    } catch (error) {
        showStatus('Network error during transaction.', 'status-error');
        console.error(error);
    } finally {
        btn.disabled = false;
        btn.textContent = 'Process Order Transaction';
        fetchProducts(); 
    }
}

function showStatus(message, className) {
    const statusBox = document.getElementById('status-message');
    statusBox.textContent = message;
    statusBox.className = `status-message ${className}`;
}
