(function() {
    const applySystemTheme = () => {
        const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
        document.documentElement.setAttribute("data-bs-theme", prefersDark ? "dark" : "light");
    };

    // exécution immédiate
    applySystemTheme();

    // écoute les changements du thème système
    window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", applySystemTheme);
})();

// Activation automatique du dark mode selon la préférence système
document.addEventListener("DOMContentLoaded", () => {
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        document.body.classList.add('dark-mode');
    }
});
