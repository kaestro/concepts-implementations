import socket
import os
import pickle
import time
import logging
import configparser

logging.basicConfig(filename='client.log', level=logging.INFO, format='%(asctime)s - %(message)s')

def get_host_port():
    # ConfigParser 객체를 생성합니다.
    config = configparser.ConfigParser()
    config.read('.client_config')

    # 설정 파일에서 호스트와 포트 읽어오기
    host = config.get('DEFAULT', 'Host')
    port = config.getint('DEFAULT', 'Port')

    return host, port

def client_program():
    host, port = get_host_port()

    try:
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # socket 객체를 생성합니다.
        client_socket.connect((host, port))  # 서버에 연결을 요청합니다.
        logging.info('Connected to the server.')
    except socket.error as e:
        logging.error(f'Failed to connect to the server: {e}')
        return

    directory_to_watch = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'dummy_client_folder')

    prev_client_files = {}  # 이전에 서버에 전송한 클라이언트의 파일들과 그들의 최종 수정 시간을 저장하는 해시 테이블

    try:
        while True:
            try:
                client_files = {file: os.stat(os.path.join(directory_to_watch, file)).st_mtime for file in os.listdir(directory_to_watch)}  # 클라이언트의 파일들과 그들의 최종 수정 시간을 저장하는 해시 테이블
            except OSError as e:
                logging.error(f'Failed to get the file metadata: {e}')
                break

            changed_files = {file: mtime for file, mtime in client_files.items() if file not in prev_client_files or mtime != prev_client_files.get(file)}  # 파일이 추가/변경되었는지 확인

            try:
                client_socket.send(pickle.dumps(changed_files))  # 해시 테이블을 직렬화하여 서버에 전송합니다.
                logging.info('Sent the file metadata of changed files to the server.')
            except socket.error as e:
                logging.error(f'Failed to send the metadata of changed files to the server: {e}')
                break

            for file, mtime in changed_files.items():
                try:
                    with open(os.path.join(directory_to_watch, file), 'rb') as f:  # 파일을 열어서
                        client_socket.send(f.read())  # 데이터를 서버에 전송합니다.
                except IOError as e:
                    logging.error(f'Failed to send the file {file} to the server: {e}')
                    continue

                logging.info(f'Sent the file {file} to the server.')
            
            prev_client_files = client_files  # 클라이언트의 파일들과 그들의 최종 수정 시간을 저장하는 해시 테이블을 업데이트합니다.

            if not client_socket.recv(1024):
                logging.info('The server has closed the connection.')
                break

            time.sleep(10)  # 10초 동안 대기합니다.
    finally:
        client_socket.close()  # 연결을 닫습니다.

if __name__ == '__main__':
    client_program()