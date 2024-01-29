# server.py
import socket
import os
import pickle

def server_program():
    host = '0.0.0.0'
    port = 12345  # 서버와 동일한 포트를 사용합니다.

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # socket 객체를 생성합니다.
    server_socket.bind((host, port))  # 서버의 호스트와 포트를 지정합니다.
    server_socket.listen()  # 클라이언트의 연결을 기다립니다.
    print('server is listening...')

    conn, address = server_socket.accept()  # 클라이언트의 연결을 수락합니다.
    print('Connection from:', address)
    conn.send('Successfully connected'.encode())  # 클라이언트에 메시지를 보냅니다.

    server_files = {}  # 서버의 파일들과 그들의 최종 수정 시간을 저장하는 해시 테이블

    while True:
        data = conn.recv(1024)  # 클라이언트로부터 데이터를 받습니다.
        if not data:
            break
        client_files = pickle.loads(data)  # 클라이언트의 파일들과 그들의 최종 수정 시간을 저장하는 해시 테이블

        for file, mtime in client_files.items():
            if file not in server_files or mtime != server_files[file]:  # 파일이 추가/변경되었는지 확인
                with open(f'dummy_server_folder/{file}', 'wb') as f:  # 받은 데이터를 파일에 씁니다.
                    f.write(conn.recv(1024))

        server_files = {file: os.stat(f'dummy_server_folder/{file}').st_mtime for file in os.listdir('dummy_server_folder')}  # 서버의 파일들과 그들의 최종 수정 시간을 업데이트

    server_socket.close()  # 소켓을 닫습니다.

if __name__ == '__main__':
    server_program()