from app.extensions import db

class Tbl_Phone_Color_Count(db.Model):
    __tablename__ = 'tbl_phone_color_count'
    fld_index = db.Column(db.Integer,primary_key=True)
    fld_phone_color = db.Column(db.String(256),unique=True)
    fld_sale_count = db.Column(db.Integer,unique=True)
