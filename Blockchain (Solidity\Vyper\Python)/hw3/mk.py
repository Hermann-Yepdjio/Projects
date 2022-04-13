""" mk.py (Must 'pip install rlp eth-utils' to run) """
import rlp
from eth_utils import keccak, to_checksum_address, to_bytes
def mk_contract_address(sender, nonce):
        sender_bytes = to_bytes(hexstr=sender)
        raw = rlp.encode([sender_bytes, nonce])
        h = keccak(raw)
        address_bytes = h[12:]
        return to_checksum_address(address_bytes)

for x in range(1,301):
        addr = mk_contract_address("0x32b098621EB740374249d400b79997b62D76e5B1",x)
        print(f"nonce: {x} contract: {addr}")
