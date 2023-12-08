import axios from "axios";

const instance = axios.create({
    // baseURL: "http://localhost:8011",
    baseURL: "http://tctech.asia:8011",
    timeout: 1000,
});

export default instance;