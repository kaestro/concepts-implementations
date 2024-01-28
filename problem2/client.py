# client.py
import socket
import os
import re

def client_program():
    host = socket.gethostname()  # 서버의 호스트 이름을 가져옵니다.
    port = 12345  # 서버와 동일한 포트를 사용합니다.

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # socket 객체를 생성합니다.
    client_socket.connect((host, port))  # 서버에 연결을 요청합니다.

    while True:
        data = client_socket.recv(1024).decode()  # 서버로부터 메시지를 받습니다.

        # 현재 file을 받고, size 메시지를 바로 받는다 가정해서
        # filename이 실제 파일을 받는 루틴과 다른 곳에서 처리되고 있는
        # 문제가 있습니다.
        if data.startswith('File '):
            filename = data.split(' ')[1]
            print(f'Received {data}')
        elif data.startswith('Size '):
            filesize = int(data.split(' ')[1])
            filepath = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'dummy_client_folder', filename)

            with open(filepath, 'wb') as file_to_write:
                bytes_received = 0
                while bytes_received < filesize:
                    bytes_to_read = min(filesize - bytes_received, 1024)
                    file_data = client_socket.recv(bytes_to_read)
                    file_to_write.write(file_data)
                    bytes_received += len(file_data)

            print(f'{filename} has been updated.')
        elif data == 'keep-alive':
            print('Received keep-alive.')
        
        else:
            print(f'Received {data}')

    client_socket.close()  # 소켓을 닫습니다.

if __name__ == '__main__':
    client_program()