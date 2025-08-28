import { createContext, useEffect, useReducer } from "react";
import { authApis, endpoints } from "./Apis";
import userReducer from "../reducers/MyUserReducer";
import cookie from 'react-cookies'

export const MyUserContext = createContext();
export const MyDispatchContext = createContext();
export const MyCartContext = createContext();

export const UserProvider = ({ children }) => {
    const [user, dispatch] = useReducer(userReducer, null);

    useEffect(() => {
        const token = cookie.load('token');
        console.log("UserProvider useEffect - Token:", token);
        if (token && !user) {
            const api = authApis();
            api.get(endpoints['current-user'])
                .then(res => {
                    if (res.data.success) {
                        console.log("User restored:", res.data.user);
                        dispatch({ type: "login", payload: res.data.user });
                    } else {
                        console.warn("Failed to restore user:", res.data.message);
                        cookie.remove('token');
                    }
                })
                .catch(err => {
                    console.error("Error restoring user:", err);
                    cookie.remove('token');
                });
        }
    }, [user]);

    console.log("UserProvider rendering with user:", user);
    return (
        <MyUserContext.Provider value={user}>
            <MyDispatchContext.Provider value={dispatch}>
                {children}
            </MyDispatchContext.Provider>
        </MyUserContext.Provider>
    );
};