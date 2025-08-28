import React, { useEffect, useState } from "react";
import { Button, Card, Col, Row, Alert } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";

const InactiveUsers = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);

    const loadUsers = async () => {
        try {
            setLoading(true);
            //  const res = await axios.get("http://localhost:8080/JobSearchWebApp/api/users/inactive");
            const res = await Apis.get(endpoints.inactiveUsers);

            setUsers(res.data);
        } catch (err) {
            console.error("❌ Lỗi khi tải danh sách user chưa kích hoạt", err);
        } finally {
            setLoading(false);
        }
    };

    const approveUser = async (userId) => {
        try {
            // await axios.put(`http://localhost:8080/JobSearchWebApp/api/users/approve/);
            await Apis.put(`${endpoints.approveUser}${userId}`);

            alert("✅ Đã kích hoạt người dùng!");
            setUsers(prev => prev.filter(u => u.id !== userId));
        } catch (err) {
            console.error("❌ Lỗi khi kích hoạt user", err);
            alert("Không thể kích hoạt người dùng.");
        }
    };

    useEffect(() => {
        loadUsers();
    }, []);

    return (
        <>
            <h3>Nhà tuyển dụng chưa kích hoạt</h3>

            {loading && <p>⏳ Đang tải...</p>}

            {users.length === 0 && !loading ? (
                <Alert variant="info">Không có nhà tuyển dụng nào chờ kích hoạt.</Alert>
            ) : (
                <Row>
                    {users.map((u) => (
                        <Col key={u.id} md={6} className="mb-3">
                            <Card>
                                <Card.Body>
                                    <Card.Title>{u.lastName} {u.firstName}</Card.Title>
                                    <Card.Text>Username: {u.username}</Card.Text>
                                    <Card.Text>Email: {u.email}</Card.Text>
                                    <Button variant="success" onClick={() => approveUser(u.id)}>Kích hoạt</Button>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            )}
        </>
    );
};

export default InactiveUsers;
