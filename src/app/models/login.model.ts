export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    userId: number;
    username: string;
    role: 'ADMIN' | 'EMPLOYEE';
    message: string;
    status:string;
}

export interface StatusResponse{
    id: number;
    action: string;
}