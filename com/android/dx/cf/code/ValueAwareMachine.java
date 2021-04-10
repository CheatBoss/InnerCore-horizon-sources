package com.android.dx.cf.code;

import com.android.dx.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public class ValueAwareMachine extends BaseMachine
{
    public ValueAwareMachine(final Prototype prototype) {
        super(prototype);
    }
    
    @Override
    public void run(final Frame frame, int i, final int n) {
        Label_0835: {
            switch (n) {
                default: {
                    Label_0816: {
                        switch (n) {
                            default: {
                                switch (n) {
                                    default: {
                                        switch (n) {
                                            default: {
                                                switch (n) {
                                                    default: {
                                                        Label_0610: {
                                                            switch (n) {
                                                                default: {
                                                                    switch (n) {
                                                                        default: {
                                                                            switch (n) {
                                                                                default: {
                                                                                    final StringBuilder sb = new StringBuilder();
                                                                                    sb.append("shouldn't happen: ");
                                                                                    sb.append(Hex.u1(n));
                                                                                    throw new RuntimeException(sb.toString());
                                                                                }
                                                                                case 46:
                                                                                case 100:
                                                                                case 104:
                                                                                case 108:
                                                                                case 112:
                                                                                case 116:
                                                                                case 120:
                                                                                case 122:
                                                                                case 124:
                                                                                case 126:
                                                                                case 128:
                                                                                case 130: {
                                                                                    break Label_0816;
                                                                                }
                                                                                case 0:
                                                                                case 79: {
                                                                                    break Label_0816;
                                                                                }
                                                                                case 54: {
                                                                                    break Label_0835;
                                                                                }
                                                                                case 18: {
                                                                                    break Label_0835;
                                                                                }
                                                                            }
                                                                            break;
                                                                        }
                                                                        case 197: {
                                                                            break Label_0610;
                                                                        }
                                                                        case 198:
                                                                        case 199: {
                                                                            break Label_0816;
                                                                        }
                                                                    }
                                                                    break;
                                                                }
                                                                case 193: {
                                                                    this.setResult(Type.INT);
                                                                    break Label_0835;
                                                                }
                                                                case 189: {
                                                                    this.setResult(((CstType)this.getAuxCst()).getClassType().getArrayType());
                                                                    break Label_0835;
                                                                }
                                                                case 188:
                                                                case 192: {
                                                                    this.setResult(((CstType)this.getAuxCst()).getClassType());
                                                                    break Label_0835;
                                                                }
                                                                case 187: {
                                                                    this.setResult(((CstType)this.getAuxCst()).getClassType().asUninitialized(i));
                                                                    break Label_0835;
                                                                }
                                                                case 190: {
                                                                    break Label_0816;
                                                                }
                                                                case 191:
                                                                case 194:
                                                                case 195: {
                                                                    break Label_0816;
                                                                }
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 183: {
                                                        final Type type = this.arg(0).getType();
                                                        if (type.isUninitialized()) {
                                                            frame.makeInitialized(type);
                                                        }
                                                        final Type type2 = ((TypeBearer)this.getAuxCst()).getType();
                                                        if (type2 == Type.VOID) {
                                                            this.clearResult();
                                                            break Label_0835;
                                                        }
                                                        this.setResult(type2);
                                                        break Label_0835;
                                                    }
                                                    case 178:
                                                    case 180:
                                                    case 182:
                                                    case 184:
                                                    case 185: {
                                                        final Type type3 = ((TypeBearer)this.getAuxCst()).getType();
                                                        if (type3 == Type.VOID) {
                                                            this.clearResult();
                                                            break Label_0835;
                                                        }
                                                        this.setResult(type3);
                                                        break Label_0835;
                                                    }
                                                    case 177:
                                                    case 179:
                                                    case 181: {
                                                        break Label_0816;
                                                    }
                                                }
                                                break;
                                            }
                                            case 171:
                                            case 172: {
                                                break Label_0816;
                                            }
                                        }
                                        break;
                                    }
                                    case 168: {
                                        this.setResult(new ReturnAddress(this.getAuxTarget()));
                                        break Label_0835;
                                    }
                                    case 132:
                                    case 133:
                                    case 134:
                                    case 135:
                                    case 136:
                                    case 137:
                                    case 138:
                                    case 139:
                                    case 140:
                                    case 141:
                                    case 142:
                                    case 143:
                                    case 144:
                                    case 145:
                                    case 146:
                                    case 147:
                                    case 148:
                                    case 149:
                                    case 150:
                                    case 151:
                                    case 152: {
                                        break Label_0816;
                                    }
                                    case 153:
                                    case 154:
                                    case 155:
                                    case 156:
                                    case 157:
                                    case 158:
                                    case 159:
                                    case 160:
                                    case 161:
                                    case 162:
                                    case 163:
                                    case 164:
                                    case 165:
                                    case 166:
                                    case 167:
                                    case 169: {
                                        break Label_0816;
                                    }
                                }
                                break;
                            }
                            case 96: {
                                this.setResult(this.getAuxType());
                                break Label_0835;
                            }
                            case 89:
                            case 90:
                            case 91:
                            case 92:
                            case 93:
                            case 94:
                            case 95: {
                                this.clearResult();
                                for (i = this.getAuxInt(); i != 0; i >>= 4) {
                                    this.addResult(this.arg((i & 0xF) - 1));
                                }
                                break Label_0835;
                            }
                            case 87:
                            case 88: {
                                this.clearResult();
                                break Label_0835;
                            }
                        }
                    }
                    break;
                }
                case 21: {
                    this.setResult(this.arg(0));
                    break;
                }
                case 20: {
                    this.setResult((TypeBearer)this.getAuxCst());
                    break;
                }
            }
        }
        this.storeResults(frame);
    }
}
