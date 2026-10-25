const products = [
  { code: "P001", name: "Indomie Goreng", price: 3500, stock: 30 },
  { code: "P003", name: "Roti Cokelat", price: 5000, stock: 20 },
  { code: "P004", name: "Teh Botol", price: 5000, stock: 24 },
  { code: "P002", name: "Aqua 600ml", price: 4000, stock: 40 }
];
const fmt = n => "Rp" + Number(n).toLocaleString("id-ID");
const select = document.querySelector("#productSelect");
const qtyInput = document.querySelector("#quantity");
const cartBody = document.querySelector("#cartBody");
const emptyMessage = document.querySelector("#emptyMessage");
const totalEl = document.querySelector("#total");
const paymentInput = document.querySelector("#payment");
const changeEl = document.querySelector("#change");
let cart = [];
let selectedIndex = -1;
let history = JSON.parse(localStorage.getItem("kasirHistoryDemo") || "[]");

function fillProducts() {
  select.innerHTML = products.map(p => `<option value="${p.code}">${p.name} - ${fmt(p.price)}</option>`).join("");
}
function total() { return cart.reduce((sum, item) => sum + item.price * item.qty, 0); }
function render() {
  cartBody.innerHTML = "";
  cart.forEach((item, i) => {
    const tr = document.createElement("tr");
    if (i === selectedIndex) tr.classList.add("selected");
    tr.innerHTML = `<td>${item.code}</td><td>${item.name}</td><td>${fmt(item.price)}</td><td>${item.qty}</td><td>${fmt(item.price * item.qty)}</td>`;
    tr.addEventListener("click", () => { selectedIndex = i; render(); });
    cartBody.appendChild(tr);
  });
  emptyMessage.hidden = cart.length > 0;
  totalEl.textContent = fmt(total());
  updateChange();
}
function updateChange() {
  const paid = Number(paymentInput.value || 0);
  changeEl.textContent = fmt(Math.max(0, paid - total()));
}
document.querySelector("#addBtn").addEventListener("click", () => {
  const product = products.find(p => p.code === select.value);
  const qty = Math.floor(Number(qtyInput.value));
  if (!product || !Number.isFinite(qty) || qty < 1) return alert("Jumlah harus minimal 1.");
  const existing = cart.find(item => item.code === product.code);
  if (existing) existing.qty += qty;
  else cart.push({ ...product, qty });
  selectedIndex = cart.length - 1;
  render();
});
document.querySelector("#removeBtn").addEventListener("click", () => {
  if (selectedIndex < 0 || !cart[selectedIndex]) return alert("Pilih baris barang pada tabel yang ingin dihapus.");
  cart.splice(selectedIndex, 1); selectedIndex = -1; render();
});
paymentInput.addEventListener("input", updateChange);
document.querySelector("#payBtn").addEventListener("click", () => {
  if (!cart.length) return alert("Belum ada barang dalam transaksi.");
  const paid = Number(paymentInput.value || 0);
  if (paid < total()) return alert("Uang pembayaran belum mencukupi.");
  const transaction = { date: new Date().toLocaleString("id-ID"), items: cart.map(i => ({...i})), total: total(), paid, change: paid-total() };
  history.unshift(transaction);
  localStorage.setItem("kasirHistoryDemo", JSON.stringify(history));
  alert("Pembayaran demo berhasil. Kembalian: " + fmt(transaction.change));
  cart = []; selectedIndex = -1; paymentInput.value = ""; render();
});
document.querySelector("#newBtn").addEventListener("click", () => {
  if (cart.length && !confirm("Kosongkan barang pada transaksi ini?")) return;
  cart = []; selectedIndex = -1; paymentInput.value = ""; qtyInput.value = 1; render();
});
document.querySelector("#historyBtn").addEventListener("click", () => {
  const content = document.querySelector("#historyContent");
  content.innerHTML = history.length ? history.map((t, i) => `<div class="history-item"><strong>Transaksi ${history.length-i}</strong> — ${t.date}<br>Total: ${fmt(t.total)} | Dibayar: ${fmt(t.paid)} | Kembalian: ${fmt(t.change)}<br>${t.items.map(it => `${it.name} (${it.qty}x)`).join(", ")}</div>`).join("") : "<p>Belum ada riwayat transaksi pada browser ini.</p>";
  document.querySelector("#historyDialog").showModal();
});
document.querySelector("#closeHistory").addEventListener("click", () => document.querySelector("#historyDialog").close());
fillProducts(); render();
