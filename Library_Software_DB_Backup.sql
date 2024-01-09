PGDMP     "                      |            Library-Software #   14.9 (Ubuntu 14.9-0ubuntu0.22.04.1) #   15.5 (Ubuntu 15.5-0ubuntu0.23.10.1) M    p           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            q           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            r           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            s           1262    33390    Library-Software    DATABASE     x   CREATE DATABASE "Library-Software" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_IN';
 "   DROP DATABASE "Library-Software";
                postgres    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            t           0    0    SCHEMA public    ACL     Q   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;
                   postgres    false    4            �            1259    34277    books    TABLE     �   CREATE TABLE public.books (
    id integer NOT NULL,
    author character varying(255),
    book_type character varying(255),
    book_class character varying(255),
    publication_year integer
);
    DROP TABLE public.books;
       public         heap    postgres    false    4            �            1259    34276    books_id_seq    SEQUENCE     �   CREATE SEQUENCE public.books_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.books_id_seq;
       public          postgres    false    4    212            u           0    0    books_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.books_id_seq OWNED BY public.books.id;
          public          postgres    false    211            �            1259    34655    fine_pdf    TABLE     �   CREATE TABLE public.fine_pdf (
    id integer NOT NULL,
    issuer_id bigint,
    file_name character varying,
    date_time timestamp with time zone,
    amount numeric(10,2),
    total_items numeric
);
    DROP TABLE public.fine_pdf;
       public         heap    postgres    false    4            �            1259    34315    issuers    TABLE     �  CREATE TABLE public.issuers (
    id integer NOT NULL,
    firstname character varying(255) NOT NULL,
    lastname character varying(255),
    roll_no integer,
    issuer_type character varying(255),
    contact_no character varying(255),
    email character varying(255),
    issuer_class character varying(255),
    unique_id character varying(255) NOT NULL,
    issuer_branch character varying(255)
);
    DROP TABLE public.issuers;
       public         heap    postgres    false    4            �            1259    34314    issuers_id_seq    SEQUENCE     �   CREATE SEQUENCE public.issuers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.issuers_id_seq;
       public          postgres    false    222    4            v           0    0    issuers_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.issuers_id_seq OWNED BY public.issuers.id;
          public          postgres    false    221            �            1259    34307    item_borrows    TABLE     �  CREATE TABLE public.item_borrows (
    id integer NOT NULL,
    library_item_id integer,
    quantity integer NOT NULL,
    borrow_date timestamp without time zone NOT NULL,
    expected_return_date date NOT NULL,
    actual_return_date date,
    return_status boolean DEFAULT false NOT NULL,
    item_condition character varying(255),
    issuer_id integer,
    fine_pdf_id integer,
    returned_on_time boolean
);
     DROP TABLE public.item_borrows;
       public         heap    postgres    false    4            �            1259    34306    item_borrows_id_seq    SEQUENCE     �   CREATE SEQUENCE public.item_borrows_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.item_borrows_id_seq;
       public          postgres    false    220    4            w           0    0    item_borrows_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.item_borrows_id_seq OWNED BY public.item_borrows.id;
          public          postgres    false    219            �            1259    34286 
   item_stock    TABLE       CREATE TABLE public.item_stock (
    id integer NOT NULL,
    quantity integer NOT NULL,
    available_quantity integer NOT NULL,
    last_stocked timestamp without time zone,
    last_checked_out timestamp without time zone,
    last_checked_in timestamp without time zone
);
    DROP TABLE public.item_stock;
       public         heap    postgres    false    4            �            1259    34285    item_stock_id_seq    SEQUENCE     �   CREATE SEQUENCE public.item_stock_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.item_stock_id_seq;
       public          postgres    false    4    214            x           0    0    item_stock_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.item_stock_id_seq OWNED BY public.item_stock.id;
          public          postgres    false    213            �            1259    34268    library_items    TABLE     �  CREATE TABLE public.library_items (
    id integer NOT NULL,
    title character varying(400) NOT NULL,
    subject character varying(255),
    item_type character varying(255) NOT NULL,
    description character varying(255),
    item_location character varying(255),
    item_language character varying(255),
    unique_id character varying(255),
    publisher character varying(255),
    book_id integer,
    magazine_id integer,
    item_stock_id integer
);
 !   DROP TABLE public.library_items;
       public         heap    postgres    false    4            �            1259    34267    library_items_id_seq    SEQUENCE     �   CREATE SEQUENCE public.library_items_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.library_items_id_seq;
       public          postgres    false    210    4            y           0    0    library_items_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.library_items_id_seq OWNED BY public.library_items.id;
          public          postgres    false    209            �            1259    34293 	   magazines    TABLE     �   CREATE TABLE public.magazines (
    id integer NOT NULL,
    published_date date NOT NULL,
    editor character varying(255)
);
    DROP TABLE public.magazines;
       public         heap    postgres    false    4            �            1259    34292    magazines_id_seq    SEQUENCE     �   CREATE SEQUENCE public.magazines_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.magazines_id_seq;
       public          postgres    false    216    4            z           0    0    magazines_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.magazines_id_seq OWNED BY public.magazines.id;
          public          postgres    false    215            �            1259    34300 
   other_item    TABLE     �   CREATE TABLE public.other_item (
    id integer NOT NULL,
    library_item_id integer,
    description character varying(300)
);
    DROP TABLE public.other_item;
       public         heap    postgres    false    4            �            1259    34299    other_item_id_seq    SEQUENCE     �   CREATE SEQUENCE public.other_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.other_item_id_seq;
       public          postgres    false    218    4            {           0    0    other_item_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.other_item_id_seq OWNED BY public.other_item.id;
          public          postgres    false    217            �            1259    34694    user_entity    TABLE     �   CREATE TABLE public.user_entity (
    id integer NOT NULL,
    username character varying(255),
    password character varying(255),
    role character varying(255)
);
    DROP TABLE public.user_entity;
       public         heap    postgres    false    4            �            1259    34693    user_entity_id_seq    SEQUENCE     �   CREATE SEQUENCE public.user_entity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.user_entity_id_seq;
       public          postgres    false    226    4            |           0    0    user_entity_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.user_entity_id_seq OWNED BY public.user_entity.id;
          public          postgres    false    225            �            1259    34654    user_fine_entity_id_seq    SEQUENCE     �   CREATE SEQUENCE public.user_fine_entity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.user_fine_entity_id_seq;
       public          postgres    false    4    224            }           0    0    user_fine_entity_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.user_fine_entity_id_seq OWNED BY public.fine_pdf.id;
          public          postgres    false    223            �           2604    34280    books id    DEFAULT     d   ALTER TABLE ONLY public.books ALTER COLUMN id SET DEFAULT nextval('public.books_id_seq'::regclass);
 7   ALTER TABLE public.books ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    212    211    212            �           2604    34658    fine_pdf id    DEFAULT     r   ALTER TABLE ONLY public.fine_pdf ALTER COLUMN id SET DEFAULT nextval('public.user_fine_entity_id_seq'::regclass);
 :   ALTER TABLE public.fine_pdf ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    224    224            �           2604    34318 
   issuers id    DEFAULT     h   ALTER TABLE ONLY public.issuers ALTER COLUMN id SET DEFAULT nextval('public.issuers_id_seq'::regclass);
 9   ALTER TABLE public.issuers ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    222    221    222            �           2604    34310    item_borrows id    DEFAULT     r   ALTER TABLE ONLY public.item_borrows ALTER COLUMN id SET DEFAULT nextval('public.item_borrows_id_seq'::regclass);
 >   ALTER TABLE public.item_borrows ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    219    220    220            �           2604    34289    item_stock id    DEFAULT     n   ALTER TABLE ONLY public.item_stock ALTER COLUMN id SET DEFAULT nextval('public.item_stock_id_seq'::regclass);
 <   ALTER TABLE public.item_stock ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    214    213    214            �           2604    34271    library_items id    DEFAULT     t   ALTER TABLE ONLY public.library_items ALTER COLUMN id SET DEFAULT nextval('public.library_items_id_seq'::regclass);
 ?   ALTER TABLE public.library_items ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    209    210    210            �           2604    34296    magazines id    DEFAULT     l   ALTER TABLE ONLY public.magazines ALTER COLUMN id SET DEFAULT nextval('public.magazines_id_seq'::regclass);
 ;   ALTER TABLE public.magazines ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    215    216            �           2604    34303    other_item id    DEFAULT     n   ALTER TABLE ONLY public.other_item ALTER COLUMN id SET DEFAULT nextval('public.other_item_id_seq'::regclass);
 <   ALTER TABLE public.other_item ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    218    218            �           2604    34697    user_entity id    DEFAULT     p   ALTER TABLE ONLY public.user_entity ALTER COLUMN id SET DEFAULT nextval('public.user_entity_id_seq'::regclass);
 =   ALTER TABLE public.user_entity ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    226    225    226            _          0    34277    books 
   TABLE DATA           T   COPY public.books (id, author, book_type, book_class, publication_year) FROM stdin;
    public          postgres    false    212   E\       k          0    34655    fine_pdf 
   TABLE DATA           \   COPY public.fine_pdf (id, issuer_id, file_name, date_time, amount, total_items) FROM stdin;
    public          postgres    false    224   2]       i          0    34315    issuers 
   TABLE DATA           �   COPY public.issuers (id, firstname, lastname, roll_no, issuer_type, contact_no, email, issuer_class, unique_id, issuer_branch) FROM stdin;
    public          postgres    false    222   �]       g          0    34307    item_borrows 
   TABLE DATA           �   COPY public.item_borrows (id, library_item_id, quantity, borrow_date, expected_return_date, actual_return_date, return_status, item_condition, issuer_id, fine_pdf_id, returned_on_time) FROM stdin;
    public          postgres    false    220   �^       a          0    34286 
   item_stock 
   TABLE DATA           w   COPY public.item_stock (id, quantity, available_quantity, last_stocked, last_checked_out, last_checked_in) FROM stdin;
    public          postgres    false    214   �`       ]          0    34268    library_items 
   TABLE DATA           �   COPY public.library_items (id, title, subject, item_type, description, item_location, item_language, unique_id, publisher, book_id, magazine_id, item_stock_id) FROM stdin;
    public          postgres    false    210   �a       c          0    34293 	   magazines 
   TABLE DATA           ?   COPY public.magazines (id, published_date, editor) FROM stdin;
    public          postgres    false    216   �d       e          0    34300 
   other_item 
   TABLE DATA           F   COPY public.other_item (id, library_item_id, description) FROM stdin;
    public          postgres    false    218   �d       m          0    34694    user_entity 
   TABLE DATA           C   COPY public.user_entity (id, username, password, role) FROM stdin;
    public          postgres    false    226   e       ~           0    0    books_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.books_id_seq', 9, true);
          public          postgres    false    211                       0    0    issuers_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.issuers_id_seq', 8, true);
          public          postgres    false    221            �           0    0    item_borrows_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.item_borrows_id_seq', 24, true);
          public          postgres    false    219            �           0    0    item_stock_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.item_stock_id_seq', 11, true);
          public          postgres    false    213            �           0    0    library_items_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.library_items_id_seq', 25, true);
          public          postgres    false    209            �           0    0    magazines_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.magazines_id_seq', 2, true);
          public          postgres    false    215            �           0    0    other_item_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.other_item_id_seq', 1, false);
          public          postgres    false    217            �           0    0    user_entity_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.user_entity_id_seq', 1, true);
          public          postgres    false    225            �           0    0    user_fine_entity_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.user_fine_entity_id_seq', 3, true);
          public          postgres    false    223            �           2606    34284    books books_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.books DROP CONSTRAINT books_pkey;
       public            postgres    false    212            �           2606    34322    issuers issuers_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.issuers
    ADD CONSTRAINT issuers_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.issuers DROP CONSTRAINT issuers_pkey;
       public            postgres    false    222            �           2606    34313    item_borrows item_borrows_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.item_borrows
    ADD CONSTRAINT item_borrows_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.item_borrows DROP CONSTRAINT item_borrows_pkey;
       public            postgres    false    220            �           2606    34291    item_stock item_stock_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.item_stock
    ADD CONSTRAINT item_stock_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.item_stock DROP CONSTRAINT item_stock_pkey;
       public            postgres    false    214            �           2606    34275     library_items library_items_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.library_items
    ADD CONSTRAINT library_items_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.library_items DROP CONSTRAINT library_items_pkey;
       public            postgres    false    210            �           2606    34298    magazines magazines_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.magazines
    ADD CONSTRAINT magazines_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.magazines DROP CONSTRAINT magazines_pkey;
       public            postgres    false    216            �           2606    34305    other_item other_item_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.other_item
    ADD CONSTRAINT other_item_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.other_item DROP CONSTRAINT other_item_pkey;
       public            postgres    false    218            �           2606    34701    user_entity user_entity_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT user_entity_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.user_entity DROP CONSTRAINT user_entity_pkey;
       public            postgres    false    226            �           2606    34662    fine_pdf user_fine_entity_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.fine_pdf
    ADD CONSTRAINT user_fine_entity_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.fine_pdf DROP CONSTRAINT user_fine_entity_pkey;
       public            postgres    false    224            �           2606    34330    library_items book_id    FK CONSTRAINT     ~   ALTER TABLE ONLY public.library_items
    ADD CONSTRAINT book_id FOREIGN KEY (book_id) REFERENCES public.books(id) NOT VALID;
 ?   ALTER TABLE ONLY public.library_items DROP CONSTRAINT book_id;
       public          postgres    false    210    3258    212            �           2606    34670 *   item_borrows item_borrows_fine_pdf_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.item_borrows
    ADD CONSTRAINT item_borrows_fine_pdf_id_fkey FOREIGN KEY (fine_pdf_id) REFERENCES public.fine_pdf(id) NOT VALID;
 T   ALTER TABLE ONLY public.item_borrows DROP CONSTRAINT item_borrows_fine_pdf_id_fkey;
       public          postgres    false    220    224    3270            �           2606    34350 (   item_borrows item_borrows_issuer_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.item_borrows
    ADD CONSTRAINT item_borrows_issuer_id_fkey FOREIGN KEY (issuer_id) REFERENCES public.issuers(id);
 R   ALTER TABLE ONLY public.item_borrows DROP CONSTRAINT item_borrows_issuer_id_fkey;
       public          postgres    false    3268    222    220            �           2606    34355 .   item_borrows item_borrows_library_item_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.item_borrows
    ADD CONSTRAINT item_borrows_library_item_id_fkey FOREIGN KEY (library_item_id) REFERENCES public.library_items(id);
 X   ALTER TABLE ONLY public.item_borrows DROP CONSTRAINT item_borrows_library_item_id_fkey;
       public          postgres    false    3256    210    220            �           2606    34335 .   library_items library_items_item_stock_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.library_items
    ADD CONSTRAINT library_items_item_stock_id_fkey FOREIGN KEY (item_stock_id) REFERENCES public.item_stock(id) NOT VALID;
 X   ALTER TABLE ONLY public.library_items DROP CONSTRAINT library_items_item_stock_id_fkey;
       public          postgres    false    210    3260    214            �           2606    34340    library_items magazine_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.library_items
    ADD CONSTRAINT magazine_id FOREIGN KEY (magazine_id) REFERENCES public.magazines(id) NOT VALID;
 C   ALTER TABLE ONLY public.library_items DROP CONSTRAINT magazine_id;
       public          postgres    false    210    3262    216            �           2606    34345 *   other_item other_item_library_item_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.other_item
    ADD CONSTRAINT other_item_library_item_id_fkey FOREIGN KEY (library_item_id) REFERENCES public.library_items(id);
 T   ALTER TABLE ONLY public.other_item DROP CONSTRAINT other_item_library_item_id_fkey;
       public          postgres    false    3256    210    218            �           2606    34663 (   fine_pdf user_fine_entity_issuer_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.fine_pdf
    ADD CONSTRAINT user_fine_entity_issuer_id_fkey FOREIGN KEY (issuer_id) REFERENCES public.issuers(id);
 R   ALTER TABLE ONLY public.fine_pdf DROP CONSTRAINT user_fine_entity_issuer_id_fkey;
       public          postgres    false    222    224    3268            _   �   x�m��N�0���������oq֑mJ-U��s��K8
 �z۟�|3��1����%G�<0�@��C��׫ނ�l�,2�"����E`����~�$�|ǫS�@g]ޒPt'�6Ӎ�F�pD!�G.��_Fm�ቝFq���`�iV=��NqO��;�v+��	��^[���B���'��󙕾�'Ϩ�[�T���_N�'���Η���\�      k   �   x�]��
�0��u��K��$�a�B(X�"�.|{kk7�����%�-���ڍ�<��2��i����1�p��Xa����OdE�e�D^�y{�$ []�H����@$� e��r��g!m{^����}q�JK��-����j8(۳��{���0Z      i   �   x�e��j�@��7#s�'�]/�#iIL �f(�(F!�߾*.
}��q��բZ�Zi�+�r-t��Y���,��y���/c7vq�#�?}<�g���Sw��Ŧ���h�m�k	��&�"����/�7!D<"��n^�/-Y���Ջ'c���}�L�q��!zA�����ό1��Y;�      g   �  x���M�A���S�\迤��d3�"��Ŭs��?Q;��z\�h�U}HOOj�BX��G�#�B�Q:��L �/�t$�}"��r�����zz�Y�����RiW�zl]�3VCTn[H���*�J%2�o��(����@eib[���Z�O$����Y{F�I�ƍ��J����t���B��i�t@/ ��s�T[�F[�(O��C�q���u���X���1�ee�ל�V�݌�Yq~6:�$�N�Z�ա���G���c��q��=��I��?',⒝�j�:��5��}�'?%k]SܳxU]���� �e�T��e}�d6�)J���_�/�O7KM8�:�U|�i��s΅����&S�=Fff�PQ�akۮ���4�q$Pݐ��?�	G󱊍9�)��n����j�m�iX(~��:�z�.=7f%��]'���)�l1�w��Av��WIށt!�t^т����{=��Tz      a   E  x�uR�u�0�����<D'��s�z��ھ�+�,	�P�Cq�AN�K���]�r��5F����苅���F�Cx�SeA�M��xt�G��Sua.���z�`^ޘ$�c���S(�L�a��ƋT]6�-�۽$z�.�v!N�W	����d�9pd�h���f�Ԏp.����*�6�w��Py��x!��$�3�E�vI�0����1�.���Y��b����;F�=ޏ�d@���aI'�ȡ{�����w\E�)�R����ܲ`�^{�qHo	��΂�g��<�?�b��3s��2G_�h��G��Kʚ�y׾�vlE��[�;�1> ���      ]   �  x����n�0�;O1/�U�p	���z�[�J��1�9Ж}��C��	U"B�Qr��f�8M����_h����cTTb��%�)�-�KYT�ق,��h�*C���Wnp�uM+�h2�2~K ���!F��"ҟ�8�&��d�hH�[�Lw��BY�!�*ύ&lʣ.䩲���s���xѴ�N��x�F,�}�[y��O�s޵���6#T�R���Z��Ե���/�V�K}7�G-+�r0F<bW��q�&�iQ�``Z,�'i0gv�� ���뺰>�ZZ��%�b�5�IB9Gmk��X�w��8a�Ĥf����}U��?��M�*m���MY�3|�?vC�j�5��،u�����h4�)M�#�Q64:��g7�̟$�6��b��C+Xj��U���Nl+�Dcv3"��9
����=D���4���T�>��f�^�y�e��d� �>(��K�/{-�J��y��a!P�P֠ෆ?�Y=��Qr܋�(�|k��ŵ��!�,6�+^gB@&��Ѽ����=Zl3)�Z�V+�A+�<DF$��O2$V�C;�G��������!P�F�Q-_�^o�ZQ�ί��!���7�f����xücU;N�5�h.�6S'	��2���`���f�M� 4�C)���f^������H�LU`~�|��xUa>q' ;�a���o����b�]���8� 3�Z      c   (   x�3�4202�5��52�t�M,R�J�I�2�!���� D@      e      x������ � �      m   "   x�3�.-�L�+��4426�v����� c�e     