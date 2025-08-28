import React, { useContext, useState } from 'react';
import { Button, Form, Col, Alert, Container } from 'react-bootstrap';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Api, { authApis } from '../configs/Apis';
import MySpinner from "./layout/MySpinner";
import { endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { MyDispatchContext, MyUserContext } from '../configs/Contexts';

const Login = () => {
    const info = [
        { label: "Tên đăng nhập", field: "username", type: "text" },
        { label: "Mật khẩu", field: "password", type: "password" }
    ];
    const [user, setUser] = useState({});
    const [msg, setMsg] = useState(null);
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();
    const dispatch = useContext(MyDispatchContext);
    const [q] = useSearchParams();

    const setState = (value, field) => {
        setUser({ ...user, [field]: value });
    };

    const login = async (e) => {
        e.preventDefault();

        // Kiểm tra đầu vào
        if (!user.username || user.username.trim() === "") {
            setMsg({ type: "danger", text: "Tên đăng nhập là bắt buộc." });
            return;
        }
        if (!user.password || user.password.trim() === "") {
            setMsg({ type: "danger", text: "Mật khẩu là bắt buộc." });
            return;
        }

        try {
            setLoading(true);
            setMsg(null);
            let res = await Api.post(endpoints['login'], { ...user });

            if (res.data.success) {
                console.log("User data:", res.data.user);
                cookie.save('token', res.data.token, {
                    path: '/',
                    secure: true,
                    sameSite: 'Strict'
                });
                console.log("Token saved to cookie:", cookie.load('token'));
                dispatch({ type: "login", payload: res.data.user });
                setMsg({ type: "success", text: res.data.message || "Đăng nhập thành công!" });

                // Điều hướng đến /secure/profile
                setTimeout(() => {
                    nav('/secure/profile');
                }, 500);
            } else {
                setMsg({ type: "danger", text: res.data.message || "Đăng nhập thất bại." });
            }
        } catch (ex) {
            console.error("Login error:", ex);
            if (ex.response) {
                setMsg({ type: "danger", text: ex.response.data.message || "Lỗi máy chủ. Vui lòng thử lại." });
            } else if (ex.request) {
                setMsg({ type: "danger", text: "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng." });
            } else {
                setMsg({ type: "danger", text: "Đã xảy ra lỗi không xác định." });
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="my-4">
            <h2>Đăng nhập</h2>
            {msg && <Alert variant={msg.type}>{msg.text}</Alert>}
            <Form onSubmit={login}>
                {info.map(i => (
                    <Form.Group key={i.field} className="mb-3">
                        <Form.Control
                            value={user[i.field] || ""}
                            onChange={e => setUser({ ...user, [i.field]: e.target.value })}
                            type={i.type}
                            placeholder={i.label}
                            required
                        />
                    </Form.Group>
                ))}
                <Form.Group className="mb-3">
                    {loading ? (
                        <MySpinner />
                    ) : (
                        <Button type="submit" variant="danger" disabled={loading}>
                            Đăng nhập
                        </Button>
                    )}
                </Form.Group>
            </Form>
        </Container>
    );
};

export default Login;