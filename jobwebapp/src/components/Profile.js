import React, { useContext } from 'react';
import { Container, Alert } from 'react-bootstrap';
import { MyUserContext } from '../configs/Contexts';

const Profile = () => {
    const user = useContext(MyUserContext);

    return (
        <Container className="my-4">
            <h2>Thông tin người dùng</h2>
            {user ? (
                <div>
                    <p><strong>Tên đăng nhập:</strong> {user.username}</p>
                    <p><strong>Vai trò:</strong> {user.role}</p>
                </div>
            ) : (
                <Alert variant="warning">Vui lòng đăng nhập để xem thông tin.</Alert>
            )}
        </Container>
    );
};

export default Profile;