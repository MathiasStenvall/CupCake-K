let user;

const loginButton = document.querySelector(".loginButton");

document.addEventListener("DOMContentLoaded", async () => {
    const response = await fetch("/getUser");

    if (response.ok){
        console.error("failed to get user");
        return;
    }

    const user = await response.json();
    console.log(user);
})


